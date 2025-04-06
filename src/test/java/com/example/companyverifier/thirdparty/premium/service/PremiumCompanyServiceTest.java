package com.example.companyverifier.thirdparty.premium.service;

import com.example.companyverifier.thirdparty.common.JsonDataLoader;
import com.example.companyverifier.thirdparty.premium.model.PremiumCompanyRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PremiumCompanyServiceTest {

    private PremiumCompanyService service;

    @BeforeEach
    void setUp() {
        JsonDataLoader loader = new JsonDataLoader(new ObjectMapper());
        service = new PremiumCompanyService(loader);
    }

    @Test
    void shouldReturnMatchingCompaniesByCinQuery() {
        List<PremiumCompanyRecord> results = service.getCompaniesByIdFragment("CJQ");
        assertFalse(results.isEmpty(), "Should return matching companies for provided id fragment");
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingCompanyFound() {
        List<PremiumCompanyRecord> results = service.getCompaniesByIdFragment("NonMatchingFragment");
        assertTrue(results.isEmpty(), "Should return empty list for non matching id fragment");
    }

}
