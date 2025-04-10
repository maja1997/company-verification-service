package com.example.companyverifier.integration.client;


import com.example.companyverifier.exception.ThirdPartyUnavailableException;
import com.example.companyverifier.integration.model.FreeApiResponseItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FreeApiClient {

    private final WebClient webClient;

    public List<FreeApiResponseItem> searchCompanies(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/free-third-party").queryParam("query", query).build())
                .retrieve()
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals,
                        res -> Mono.error(new ThirdPartyUnavailableException("Free API unavailable")))
                .bodyToFlux(FreeApiResponseItem.class)
                .collectList()
                .block();

    }
}
