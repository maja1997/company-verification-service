package com.example.companyverifier.integration.client;

import com.example.companyverifier.exception.ThirdPartyUnavailableException;
import com.example.companyverifier.integration.model.FreeApiResponseItem;
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

class FreeApiClientTest {

    private ExchangeFunction exchangeFunction;
    private FreeApiClient client;

    @BeforeEach
    void setUp() {
        exchangeFunction = mock(ExchangeFunction.class);
        WebClient webClient = WebClient.builder().exchangeFunction(exchangeFunction).build();
        client = new FreeApiClient(webClient);
    }

    @Test
    void shouldReturnCompanyListWhenApiSucceeds() {
        String query = "search-query";
        ClientResponse mockResponse = ClientResponse.create(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body("[{\"cin\":\"123\",\"companyName\":\"Test Ltd\",\"registration_date\":\"2022-01-01\",\"fullAddress\":\"Test Address\",\"is_active\":true}]")
                .build();

        when(exchangeFunction.exchange(any())).thenReturn(Mono.just(mockResponse));

        List<FreeApiResponseItem> result = client.searchCompanies(query);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCin()).isEqualTo("123");
        assertThat(result.get(0).isActive()).isTrue();
    }

    @Test
    void shouldThrowThirdPartyUnavailableExceptionOn503() {
        String query = "fails";
        ClientResponse errorResponse = ClientResponse.create(HttpStatus.SERVICE_UNAVAILABLE)
                .header("Content-Type", "application/json")
                .build();

        when(exchangeFunction.exchange(any())).thenReturn(Mono.just(errorResponse));

        assertThatThrownBy(() -> client.searchCompanies(query))
                .isInstanceOf(ThirdPartyUnavailableException.class)
                .hasMessageContaining("Free API unavailable");
    }
}
