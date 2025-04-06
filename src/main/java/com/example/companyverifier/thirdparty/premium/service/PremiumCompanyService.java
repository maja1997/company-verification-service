package com.example.companyverifier.thirdparty.premium.service;

import com.example.companyverifier.thirdparty.common.JsonDataLoader;
import com.example.companyverifier.thirdparty.premium.model.PremiumCompanyRecord;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PremiumCompanyService {

    private List<PremiumCompanyRecord> companies;

    public PremiumCompanyService(JsonDataLoader loader) {
        try {
            this.companies = loader.load("static/premium_service_companies-1.json", new TypeReference<>() {});
        } catch (IOException exception) {
            throw new RuntimeException("Failed to load company data from premium API", exception);
        }
    }

    public List<PremiumCompanyRecord> getCompaniesByIdFragment(String idFragment) {
        return companies.stream()
                .filter(company -> company.getCompanyIdentificationNumber() != null
                        && company.getCompanyIdentificationNumber().toLowerCase().contains(idFragment.toLowerCase()))
                .toList();
    }
}
