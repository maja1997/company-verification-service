package com.example.companyverifier.integration.client;

import com.example.companyverifier.exception.ThirdPartyUnavailableException;
import com.example.companyverifier.integration.model.PremiumApiResponseItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PremiumApiClient {

    private final WebClient webClient;

    public List<PremiumApiResponseItem> searchCompanies(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/premium-third-party")
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals,
                        res -> Mono.error(new ThirdPartyUnavailableException("Premium API unavailable")))
                .bodyToFlux(PremiumApiResponseItem.class)
                .collectList()
                .block();
    }
}
