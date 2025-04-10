package com.example.companyverifier.integration.service;

import com.example.companyverifier.integration.client.PremiumApiClient;
import com.example.companyverifier.integration.mapper.PremiumApiMapper;
import com.example.companyverifier.integration.model.PremiumApiResponseItem;
import com.example.companyverifier.model.CompanyData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PremiumApiService {

    private final PremiumApiClient client;

    public List<CompanyData> searchActiveCompanies(String query) {
        return client.searchCompanies(query).stream()
                .filter(PremiumApiResponseItem::isActive)
                .map(PremiumApiMapper::toCompanyData)
                .toList();
    }
}
