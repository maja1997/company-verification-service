package com.example.companyverifier.integration.mapper;

import com.example.companyverifier.integration.model.FreeApiResponseItem;
import com.example.companyverifier.model.CompanyData;

public class FreeApiMapper {

    public static CompanyData toCompanyData(FreeApiResponseItem item) {
        return CompanyData.builder()
                .companyIdentificationNumber(item.getCin())
                .companyName(item.getName())
                .registrationDate(item.getRegistrationDate())
                .companyFullAddress(item.getAddress())
                .isActive(item.isActive())
                .build();
    }
}
