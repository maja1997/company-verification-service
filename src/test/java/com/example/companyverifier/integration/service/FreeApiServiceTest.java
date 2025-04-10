package com.example.companyverifier.integration.service;

import com.example.companyverifier.integration.client.FreeApiClient;
import com.example.companyverifier.integration.model.FreeApiResponseItem;
import com.example.companyverifier.model.CompanyData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FreeApiServiceTest {

    @Mock
    private FreeApiClient client;
    @InjectMocks
    private FreeApiService service;

    @Test
    void shouldReturnOnlyMappedActiveCompanies() {
        String query = "query";

        FreeApiResponseItem active1 = FreeApiResponseItem.builder()
                .cin("1")
                .name("Active Company 1")
                .registrationDate("2021-01-01")
                .address("Street 1")
                .isActive(true)
                .build();

        FreeApiResponseItem inactive = FreeApiResponseItem.builder()
                .cin("2")
                .name("Inactive Company")
                .registrationDate("2010-01-01")
                .address("Old Street")
                .isActive(false)
                .build();

        FreeApiResponseItem active2 = FreeApiResponseItem.builder()
                .cin("3")
                .name("Active Company 2")
                .registrationDate("2024-01-01")
                .address("Street 2")
                .isActive(true)
                .build();

        when(client.searchCompanies(query)).thenReturn(List.of(active1, inactive, active2));

        List<CompanyData> result = service.searchActiveCompanies(query);

        assertThat(result)
                .hasSize(2)
                .extracting(CompanyData::getCompanyIdentificationNumber)
                .containsExactlyInAnyOrder("1", "3");

        verify(client).searchCompanies(query);
    }
}
