package com.example.companyverifier.document;

import com.example.companyverifier.model.CompanyData;
import com.example.companyverifier.model.CompanyDataSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Document(collection = "verifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationDocument {

    @Id
    private UUID verificationId;

    private String queryText;

    private Instant timestamp;

    private Object result;

    private List<CompanyData> otherResults;

    private CompanyDataSource source;
}
