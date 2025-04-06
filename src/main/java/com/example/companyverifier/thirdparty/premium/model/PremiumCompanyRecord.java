package com.example.companyverifier.thirdparty.premium.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PremiumCompanyRecord {

    private String companyIdentificationNumber;

    private String companyName;

    private String registrationDate;

    @JsonProperty("fullAddress")
    private String companyFullAddress;

    @JsonProperty("isActive")
    private boolean active;

    @JsonProperty("companyFullAddress")
    public String getCompanyFullAddress() {
        return companyFullAddress;
    }
}
