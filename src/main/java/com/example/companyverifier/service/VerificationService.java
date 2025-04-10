package com.example.companyverifier.service;

import com.example.companyverifier.document.VerificationDocument;
import com.example.companyverifier.exception.ThirdPartyUnavailableException;
import com.example.companyverifier.integration.service.FreeApiService;
import com.example.companyverifier.integration.service.PremiumApiService;
import com.example.companyverifier.model.CompanyData;
import com.example.companyverifier.model.CompanyDataSource;
import com.example.companyverifier.model.VerificationResponse;
import com.example.companyverifier.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationService {

    private final FreeApiService freeAPIService;
    private final PremiumApiService premiumAPIService;
    private final VerificationRepository verificationRepository;

    public VerificationResponse verifyCompany(UUID verificationId, String query) {
        ApiCallResult free = attemptCompanySearch(() -> freeAPIService.searchActiveCompanies(query), CompanyDataSource.FREE);
        ApiCallResult premium = null;
        ApiCallResult responseResult = null;

        if (free.hasResult()) {
            responseResult = free;
        } else {
            premium = attemptCompanySearch(() -> premiumAPIService.searchActiveCompanies(query), CompanyDataSource.PREMIUM);
            if (premium.hasResult()) {
                responseResult = premium;
            }
        }

        if (responseResult == null) {
            return handleNoMatchResult(verificationId, query, free, premium);
        }

        return handleSuccessfulResult(verificationId, query, responseResult);
    }

    public VerificationResponse getVerificationById(UUID verificationId) {
        log.info("Retrieving verification result for ID: {}", verificationId);

        VerificationDocument document = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Verification not found"));

        return VerificationResponse.builder()
                .verificationId(document.getVerificationId())
                .query(document.getQueryText())
                .result(document.getResult())
                .otherResults(document.getOtherResults())
                .build();
    }

    private VerificationResponse handleSuccessfulResult(UUID verificationId, String query, ApiCallResult result) {
        CompanyData primary = result.companies().get(0);
        List<CompanyData> other = result.companies().size() > 1
                ? result.companies().subList(1, result.companies().size())
                : null;

        persistVerificationResult(verificationId, query, primary, other, result.source());

        log.info("Returning response from {} service with {} additional match(es)",
                result.source(), other != null ? other.size() : 0);

        return VerificationResponse.builder()
                .verificationId(verificationId)
                .query(query)
                .result(primary)
                .otherResults(other)
                .build();
    }

    private VerificationResponse handleNoMatchResult(UUID verificationId, String query, ApiCallResult free, ApiCallResult premium) {
        String message = (free.failed() && premium.failed())
                ? "Both third-party services are unavailable."
                : "No active companies found in either service.";

        log.warn("No company match found. Reason: {}", message);

        persistVerificationResult(verificationId, query, Map.of("message", message), null, null);

        return VerificationResponse.builder()
                .verificationId(verificationId)
                .query(query)
                .result(Map.of("message", message))
                .build();
    }

    private ApiCallResult attemptCompanySearch(Supplier<List<CompanyData>> apiCall, CompanyDataSource source) {
        try {
            log.info("Calling {} service for company search...", source);
            List<CompanyData> companies = apiCall.get();
            if (companies == null || companies.isEmpty()) {
                log.info("{} service returned no active companies.", source);
            } else {
                log.info("{} service returned {} active companies.", source, companies.size());
            }
            return new ApiCallResult(
                    (companies == null || companies.isEmpty()) ? null : companies,
                    source,
                    false
            );
        } catch (ThirdPartyUnavailableException exception) {
            log.warn("{} service failed with exception: {}", source, exception.getMessage());
            return new ApiCallResult(null, source, true);
        }
    }

    private void persistVerificationResult(UUID verificationId, String query, Object result,
                                           List<CompanyData> otherResults, CompanyDataSource source) {
        verificationRepository.save(
                VerificationDocument.builder()
                        .verificationId(verificationId)
                        .queryText(query)
                        .timestamp(Instant.now())
                        .result(result)
                        .otherResults(otherResults)
                        .source(source)
                        .build()
        );
    }

    private record ApiCallResult(List<CompanyData> companies, CompanyDataSource source, boolean failed) {
        boolean hasResult() {
            return companies != null && !companies.isEmpty();
        }
    }
}
