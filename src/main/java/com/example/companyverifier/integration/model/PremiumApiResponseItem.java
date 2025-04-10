package com.example.companyverifier.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PremiumApiResponseItem {

    private String companyIdentificationNumber;

    private String companyName;

    private String registrationDate;

    @JsonProperty("fullAddress")
    private String companyFullAddress;

    @JsonProperty("isActive")
    private boolean active;
}
