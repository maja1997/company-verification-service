package com.example.companyverifier.thirdparty.free.dataprovider;

import com.example.companyverifier.thirdparty.free.model.FreeCompanyRecord;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class FreeCompanyDataProvider {

    private final List<FreeCompanyRecord> companies;

    public FreeCompanyDataProvider(ObjectMapper objectMapper) throws Exception {
        InputStream inputStream = new ClassPathResource("static/free_service_companies-1.json").getInputStream();
        this.companies = objectMapper.readValue(inputStream, new TypeReference<>() {
        });
    }

    public List<FreeCompanyRecord> getCompanies() {
        return companies;
    }
}
