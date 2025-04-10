package com.example.companyverifier.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerificationResponse {

    private UUID verificationId;
    private String query;
    private Object result;
    private List<CompanyData> otherResults;

}
