package com.example.companyverifier.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FreeApiResponseItem {

    private String cin;

    private String name;

    @JsonProperty("registration_date")
    private String registrationDate;

    private String address;

    @JsonProperty("is_active")
    private boolean isActive;
}
