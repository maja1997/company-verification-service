package com.example.companyverifier.integration.client;

import com.example.companyverifier.exception.ThirdPartyUnavailableException;
import com.example.companyverifier.integration.model.PremiumApiResponseItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class PremiumApiClientTest {

    private ExchangeFunction exchangeFunction;
    private PremiumApiClient client;

    @BeforeEach
    void setUp() {
        exchangeFunction = mock(ExchangeFunction.class);
        WebClient webClient = WebClient.builder().exchangeFunction(exchangeFunction).build();
        client = new PremiumApiClient(webClient);
    }

    @Test
    void shouldReturnCompanyListWhenApiSucceeds() {
        String query = "sample";

        ClientResponse mockResponse = ClientResponse.create(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body("""
                    [{
                        "companyIdentificationNumber": "1",
                        "companyName": "Premium Co",
                        "registrationDate": "2023-01-01",
                        "fullAddress": "Premium Street",
                        "isActive": true
                    }]
                """)
                .build();

        when(exchangeFunction.exchange(any())).thenReturn(Mono.just(mockResponse));

        List<PremiumApiResponseItem> result = client.searchCompanies(query);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCompanyIdentificationNumber()).isEqualTo("1");
        assertThat(result.get(0).getCompanyName()).isEqualTo("Premium Co");
        assertThat(result.get(0).isActive()).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenServiceIsUnavailable() {
        String query = "any";

        ClientResponse errorResponse = ClientResponse.create(HttpStatus.SERVICE_UNAVAILABLE)
                .header("Content-Type", "application/json")
                .build();

        when(exchangeFunction.exchange(any())).thenReturn(Mono.just(errorResponse));

        assertThatThrownBy(() -> client.searchCompanies(query))
                .isInstanceOf(ThirdPartyUnavailableException.class)
                .hasMessageContaining("Premium API unavailable");
    }
}
