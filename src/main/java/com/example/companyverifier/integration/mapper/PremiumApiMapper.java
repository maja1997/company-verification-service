package com.example.companyverifier.integration.mapper;

import com.example.companyverifier.integration.model.PremiumApiResponseItem;
import com.example.companyverifier.model.CompanyData;

public class PremiumApiMapper {

    public static CompanyData toCompanyData(PremiumApiResponseItem item) {
        return CompanyData.builder()
                .companyIdentificationNumber(item.getCompanyIdentificationNumber())
                .companyName(item.getCompanyName())
                .registrationDate(item.getRegistrationDate())
                .companyFullAddress(item.getCompanyFullAddress())
                .isActive(item.isActive())
                .build();
    }
}
