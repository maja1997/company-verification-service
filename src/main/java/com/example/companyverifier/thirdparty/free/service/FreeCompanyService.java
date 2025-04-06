package com.example.companyverifier.thirdparty.free.service;

import com.example.companyverifier.thirdparty.free.dataprovider.FreeCompanyDataProvider;
import com.example.companyverifier.thirdparty.free.model.FreeCompanyRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FreeCompanyService {

    private final FreeCompanyDataProvider dataProvider;

    public List<FreeCompanyRecord> getByCinQuery(String cinQuery) {
        return dataProvider.getCompanies().stream()
                .filter(company -> company.getCin() != null
                        && company.getCin().toLowerCase().contains(cinQuery.toLowerCase()))
                .toList();
    }
}
