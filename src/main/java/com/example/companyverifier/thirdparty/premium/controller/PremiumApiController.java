package com.example.companyverifier.thirdparty.premium.controller;

import com.example.companyverifier.thirdparty.premium.model.PremiumCompanyRecord;
import com.example.companyverifier.thirdparty.premium.service.PremiumCompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/premium-third-party")
public class PremiumApiController {

    private final PremiumCompanyService companyService;
    private final Random random = new Random();

    public PremiumApiController(PremiumCompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<PremiumCompanyRecord>> getCompaniesByIdFragment(@RequestParam("query") String idFragment) {
        if (random.nextDouble() < 0.1) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        return ResponseEntity.ok(companyService.getCompaniesByIdFragment(idFragment));
    }

}
