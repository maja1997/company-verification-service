package com.example.companyverifier.controller;

import com.example.companyverifier.model.VerificationResponse;
import com.example.companyverifier.service.VerificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VerificationController.class)
class VerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VerificationService verificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldVerifyCompanySuccessfully() throws Exception {
        UUID verificationId = UUID.randomUUID();
        String query = "sample";
        VerificationResponse response = VerificationResponse.builder()
                .verificationId(verificationId)
                .query(query)
                .result(Map.of("company", "Test Co"))
                .build();

        Mockito.when(verificationService.verifyCompany(eq(verificationId), eq(query)))
                .thenReturn(response);

        mockMvc.perform(get("/backend-service")
                        .param("verificationId", verificationId.toString())
                        .param("query", query))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void shouldReturnBadRequestForBlankQuery() throws Exception {
        UUID verificationId = UUID.randomUUID();

        mockMvc.perform(get("/backend-service")
                        .param("verificationId", verificationId.toString())
                        .param("query", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnStoredVerification() throws Exception {
        UUID id = UUID.randomUUID();
        VerificationResponse response = VerificationResponse.builder()
                .verificationId(id)
                .query("previous")
                .result(Map.of("message", "stored response"))
                .build();

        Mockito.when(verificationService.getVerificationById(id))
                .thenReturn(response);

        mockMvc.perform(get("/backend-service/verification/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
