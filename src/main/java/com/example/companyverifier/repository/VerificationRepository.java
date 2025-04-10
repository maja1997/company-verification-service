package com.example.companyverifier.repository;

import com.example.companyverifier.document.VerificationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface VerificationRepository extends MongoRepository<VerificationDocument, UUID> {
}
