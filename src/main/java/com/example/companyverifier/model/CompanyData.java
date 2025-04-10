package com.example.companyverifier.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CompanyData {

    private String companyName;
    private String companyIdentificationNumber;
    private String registrationDate;
    private String companyFullAddress;
    private boolean isActive;
}
