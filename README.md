# company-verification-service

This service verifies company data by querying two third-party APIs (Free and Premium) and returns structured responses based on available matches. The verification result is persisted in a MongoDB collection and can be retrieved later using the verification ID.

___

## Endpoints

- `GET /backend-service?verificationId={uuid}&query={text}`  
  Verifies a company and returns the result.

- `GET /backend-service/verification/{id}`  
  Retrieves previously stored verification result.

---

## Running Locally with Docker

### Prerequisites
- Java 17+
- Docker & Docker Compose installed

### 1. Start MongoDB with Docker

```bash
docker-compose up -d
```

### 2. Run the app

```bash
./gradlew bootRun
```
The service will be available at: http://localhost:8080
