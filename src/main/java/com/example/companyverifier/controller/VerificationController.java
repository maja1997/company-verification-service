package com.example.companyverifier.controller;

import com.example.companyverifier.model.VerificationResponse;
import com.example.companyverifier.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/backend-service")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    @GetMapping
    public ResponseEntity<VerificationResponse> verifyCompany(
            @RequestParam UUID verificationId,
            @RequestParam String query
    ) {
        if (query == null || query.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Query must not be blank");
        }

        VerificationResponse response = verificationService.verifyCompany(verificationId, query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verification/{id}")
    public ResponseEntity<VerificationResponse> getVerification(@PathVariable UUID id) {
        return ResponseEntity.ok(verificationService.getVerificationById(id));
    }
}
