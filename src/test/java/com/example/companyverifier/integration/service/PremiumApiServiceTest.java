package com.example.companyverifier.integration.service;

import com.example.companyverifier.integration.client.PremiumApiClient;
import com.example.companyverifier.integration.model.PremiumApiResponseItem;
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
class PremiumApiServiceTest {

    @Mock
    private PremiumApiClient client;
    @InjectMocks
    private PremiumApiService service;

    @Test
    void shouldReturnOnlyMappedActiveCompanies() {
        String query = "query";

        PremiumApiResponseItem active1 = PremiumApiResponseItem.builder()
                .companyIdentificationNumber("1")
                .companyName("Active Premium 1")
                .registrationDate("2021-01-01")
                .companyFullAddress("Premium Street 1")
                .active(true)
                .build();

        PremiumApiResponseItem inactive = PremiumApiResponseItem.builder()
                .companyIdentificationNumber("2")
                .companyName("Inactive Premium")
                .registrationDate("2009-01-01")
                .companyFullAddress("Old Address")
                .active(false)
                .build();

        PremiumApiResponseItem active2 = PremiumApiResponseItem.builder()
                .companyIdentificationNumber("3")
                .companyName("Active Premium 2")
                .registrationDate("2022-05-10")
                .companyFullAddress("Premium Street 2")
                .active(true)
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
