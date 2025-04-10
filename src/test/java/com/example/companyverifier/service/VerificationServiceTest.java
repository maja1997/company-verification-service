package com.example.companyverifier.service;

import com.example.companyverifier.document.VerificationDocument;
import com.example.companyverifier.exception.ThirdPartyUnavailableException;
import com.example.companyverifier.integration.service.FreeApiService;
import com.example.companyverifier.integration.service.PremiumApiService;
import com.example.companyverifier.model.CompanyData;
import com.example.companyverifier.model.CompanyDataSource;
import com.example.companyverifier.model.VerificationResponse;
import com.example.companyverifier.repository.VerificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificationServiceTest {

    @Mock
    private FreeApiService freeApiService;
    @Mock
    private PremiumApiService premiumApiService;
    @Mock
    private VerificationRepository verificationRepository;
    @InjectMocks
    private VerificationService verificationService;
    @Captor
    private ArgumentCaptor<VerificationDocument> verificationDocumentCaptor;

    @Test
    void shouldReturnResponseFromFreeApiWhenItHasActiveCompanies() {
        UUID id = UUID.randomUUID();
        String query = "testQuery";
        CompanyData company = CompanyData.builder()
                .companyIdentificationNumber("123")
                .companyName("Test Company")
                .registrationDate("2022-01-01")
                .companyFullAddress("Test Address")
                .isActive(true)
                .build();

        when(freeApiService.searchActiveCompanies(query)).thenReturn(List.of(company));

        VerificationResponse response = verificationService.verifyCompany(id, query);

        assertThat(response.getResult()).isEqualTo(company);
        assertThat(response.getOtherResults()).isNull();

        verify(verificationRepository).save(verificationDocumentCaptor.capture());
        assertThat(verificationDocumentCaptor.getValue().getSource()).isEqualTo(CompanyDataSource.FREE);
    }

    @Test
    void shouldFallbackToPremiumApiWhenFreeFails() {
        UUID id = UUID.randomUUID();
        String query = "testQuery";
        CompanyData premiumCompany = CompanyData.builder()
                .companyIdentificationNumber("456")
                .companyName("Premium Company")
                .registrationDate("2020-12-12")
                .companyFullAddress("Premium Address")
                .isActive(true)
                .build();

        when(freeApiService.searchActiveCompanies(query))
                .thenThrow(new ThirdPartyUnavailableException("Free API unavailable"));
        when(premiumApiService.searchActiveCompanies(query))
                .thenReturn(List.of(premiumCompany));

        VerificationResponse response = verificationService.verifyCompany(id, query);

        assertThat(response.getResult()).isEqualTo(premiumCompany);
        verify(verificationRepository).save(any());
    }

    @Test
    void shouldReturnMessageWhenNoResultsAndNoFailure() {
        UUID id = UUID.randomUUID();
        String query = "test";

        when(freeApiService.searchActiveCompanies(query)).thenReturn(List.of());
        when(premiumApiService.searchActiveCompanies(query)).thenReturn(List.of());

        VerificationResponse response = verificationService.verifyCompany(id, query);

        assertThat(response.getResult()).isInstanceOf(Map.class);
        assertThat(((Map<?, ?>) response.getResult()).get("message"))
                .isEqualTo("No active companies found in either service.");
    }

    @Test
    void shouldReturnMessageWhenBothApisFail() {
        UUID id = UUID.randomUUID();
        String query = "test";

        when(freeApiService.searchActiveCompanies(query))
                .thenThrow(new ThirdPartyUnavailableException("Free unavailable"));
        when(premiumApiService.searchActiveCompanies(query))
                .thenThrow(new ThirdPartyUnavailableException("Premium unavailable"));

        VerificationResponse response = verificationService.verifyCompany(id, query);

        assertThat(response.getResult()).isInstanceOf(Map.class);
        assertThat(((Map<?, ?>) response.getResult()).get("message"))
                .isEqualTo("Both third-party services are unavailable.");
    }

    @Test
    void shouldRetrieveStoredVerificationById() {
        UUID id = UUID.randomUUID();

        VerificationDocument doc = VerificationDocument.builder()
                .verificationId(id)
                .queryText("someQuery")
                .timestamp(Instant.now())
                .result(Map.of("message", "test"))
                .build();

        when(verificationRepository.findById(id)).thenReturn(Optional.of(doc));

        VerificationResponse response = verificationService.getVerificationById(id);

        assertThat(response.getVerificationId()).isEqualTo(id);
        assertThat(response.getResult()).isEqualTo(doc.getResult());
    }
}
