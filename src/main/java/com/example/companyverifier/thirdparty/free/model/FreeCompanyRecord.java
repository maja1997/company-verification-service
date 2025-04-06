package com.example.companyverifier.thirdparty.free.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FreeCompanyRecord {

    private String cin;

    private String name;

    @JsonProperty("registration_date")
    private String registrationDate;

    private String address;

    @JsonProperty("is_active")
    private boolean isActive;
}
