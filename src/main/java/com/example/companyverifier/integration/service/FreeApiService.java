package com.example.companyverifier.integration.service;

import com.example.companyverifier.integration.client.FreeApiClient;
import com.example.companyverifier.integration.mapper.FreeApiMapper;
import com.example.companyverifier.integration.model.FreeApiResponseItem;
import com.example.companyverifier.model.CompanyData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FreeApiService {

    private final FreeApiClient client;

    public List<CompanyData> searchActiveCompanies(String query) {
        return client.searchCompanies(query).stream()
                .filter(FreeApiResponseItem::isActive)
                .map(FreeApiMapper::toCompanyData)
                .toList();
    }
}
