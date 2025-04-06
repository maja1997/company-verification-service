package com.example.companyverifier.thirdparty.free.service;

import com.example.companyverifier.thirdparty.free.dataprovider.FreeCompanyDataProvider;
import com.example.companyverifier.thirdparty.free.model.FreeCompanyRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FreeCompanyDataProviderTest {

    private FreeCompanyDataProvider dataProvider;

    @BeforeEach
    void setUp() throws Exception {
        dataProvider = new FreeCompanyDataProvider(new ObjectMapper());
    }

    @Test
    void shouldLoadCompaniesFromJson() {
        List<FreeCompanyRecord> companies = dataProvider.getCompanies();
        assertNotNull(companies, "Companies list should not be null");
        assertFalse(companies.isEmpty(), "Companies list should not be empty");
    }
}
