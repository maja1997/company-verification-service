package com.example.companyverifier.thirdparty.free.service;

import com.example.companyverifier.thirdparty.common.JsonDataLoader;
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
    void setUp() {
        JsonDataLoader loader = new JsonDataLoader(new ObjectMapper());
        service = new FreeCompanyService(loader);
    }

    @Test
    void shouldReturnMatchingCompaniesByCinQuery() {
        List<FreeCompanyRecord> results = service.getCompaniesByIdFragment("CJQ");
        assertFalse(results.isEmpty(), "Should return matching companies for provided id fragment");
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingCompanyFound() {
        List<FreeCompanyRecord> results = service.getCompaniesByIdFragment("NonMatchingFragment");
        assertTrue(results.isEmpty(), "Should return empty list for non matching id fragment");
    }

}
