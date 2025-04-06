package com.example.companyverifier.thirdparty.free.service;

import com.example.companyverifier.thirdparty.common.JsonDataLoader;
import com.example.companyverifier.thirdparty.free.model.FreeCompanyRecord;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class FreeCompanyService {

    private final List<FreeCompanyRecord> companies;

    public FreeCompanyService(JsonDataLoader loader) {
        try {
            this.companies = loader.load("static/free_service_companies-1.json", new TypeReference<>() {});
        } catch (IOException exception) {
            throw new RuntimeException("Failed to load company data from free API", exception);
        }
    }

    public List<FreeCompanyRecord> getCompaniesByIdFragment(String idFragment) {
        return companies.stream()
                .filter(company -> company.getCin() != null
                        && company.getCin().toLowerCase().contains(idFragment.toLowerCase()))
                .toList();
    }
}
