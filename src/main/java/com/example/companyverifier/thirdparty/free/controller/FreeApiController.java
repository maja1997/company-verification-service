package com.example.companyverifier.thirdparty.free.controller;

import com.example.companyverifier.thirdparty.free.model.FreeCompanyRecord;
import com.example.companyverifier.thirdparty.free.service.FreeCompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/free-third-party")
public class FreeApiController {

    private final FreeCompanyService companyService;
    private final Random random = new Random();

    public FreeApiController(FreeCompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<FreeCompanyRecord>> getCompanies(@RequestParam String cinQuery) {
        if (random.nextDouble() < 0.4) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        return ResponseEntity.ok(companyService.getByCinQuery(cinQuery));
    }


}
