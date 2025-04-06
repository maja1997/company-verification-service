package com.example.companyverifier.thirdparty.free.service;

import com.example.companyverifier.thirdparty.free.dataprovider.FreeCompanyDataProvider;
import com.example.companyverifier.thirdparty.free.model.FreeCompanyRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FreeCompanyServiceTest {

    private FreeCompanyService service;

    @BeforeEach
    void setUp() throws Exception {
        FreeCompanyDataProvider dataProvider = new FreeCompanyDataProvider(new ObjectMapper());
        service = new FreeCompanyService(dataProvider);
    }

    @Test
    void shouldReturnMatchingCompaniesByCinQuery() {
        List<FreeCompanyRecord> results = service.getByCinQuery("CJQ");
        assertFalse(results.isEmpty(), "Should return matching companies");
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingCompanyFound() {
        List<FreeCompanyRecord> results = service.getByCinQuery("AAA");
        assertTrue(results.isEmpty(), "Should return empty list for non matching query");
    }

}
