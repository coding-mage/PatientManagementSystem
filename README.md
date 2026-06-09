# Patient Management System (Microservices)

This repository contains a Java-based microservices platform for a patient management healthcare system. It implements clinical scheduling, patient intake, billing management, and analytics utilizing Spring Boot, Spring Cloud Gateway, gRPC, Apache Kafka, PostgreSQL, and Elasticsearch.

## Architecture

The platform comprises the following microservices and infrastructure components:

- **api-gateway** (Port 4007): Public entry point that performs JWT-based authentication and role enforcement.
- **auth-service** (Port 4005 / gRPC 9097): Manages user accounts, authenticates credentials, and issues/validates JWTs.
- **patient-service** (Port 4000): Manages patient demography. Triggers gRPC billing account creation.
- **billing-service** (Port 4002 / gRPC 9095): Manages billing balance ledgers, charges, and credits.
- **appointment-service** (Port 4006): Manages appointments, calculates duration overlaps, and processes fees.
- **analytics-service** (Port 4004): Consumes events asynchronously via Kafka and persists metrics.

Each service operates with an independent PostgreSQL database.

## System Interconnections

```
                         +-------------+
                         | Client Apps |
                         +------+------+
                                | HTTP + JWT
                                v
                       +-----------------+
                       |   API Gateway   |
                       +--------+--------+
                                |
       +-----------+------------+------------+-----------+
       |           |                         |           |
       v           v                         v           v
  +----+---+   +---+----+               +----+----+  +---+----+
  |  Auth  |   | Patient|               | Appoint.|  |Analytics|
  +--------+   +---+----+               +----+----+  +---+----+
                   |                         |           ^
                   | gRPC                    | gRPC      |
                   v                         v           | Kafka
               +---+----+                    |           | Events
               | Billing|<-------------------+           |
               +--------+                                |
                   |                                     |
                   +-------------------------------------+
```

- **Patient Registration**: Creating a patient synchronously provisions a zero-balance billing account over gRPC.
- **Appointment Fees**: Scheduling an appointment automatically charges the billing account. Rescheduling or cancellations compute the delta fee and apply charge adjustments or credits respectively.
- **Event Audit**: Lifecycle events are emitted to Kafka topics and consumed asynchronously by the analytics service.

## Prerequisites

- Java 21 JDK
- Maven 3.9+
- Docker and Docker Compose

## Quick Start

Run the entire platform including databases, Kafka, and the ELK logging stack using Docker Compose:

```bash
docker compose up --build -d
```

### Verification Flow

1. **Create User**:
   ```bash
   curl -X POST http://localhost:4007/auth/users \
     -H "Content-Type: application/json" \
     -d '{"email":"staff@clinic.com","password":"password123","role":"RECEPTIONIST"}'
   ```

2. **Authenticate**:
   ```bash
   curl -X POST http://localhost:4007/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email":"staff@clinic.com","password":"password123"}'
   ```
   Save the returned JWT token to authenticate subsequent requests.

3. **Register Patient**:
   ```bash
   curl -X POST http://localhost:4007/api/patients \
     -H "Authorization: Bearer <token>" \
     -H "Content-Type: application/json" \
     -d '{"name":"Jane Doe","email":"jane.doe@example.com","address":"123 Main St","dateOfBirth":"1990-01-01"}'
   ```

## Microservice-Specific Documentation

For detailed configuration properties, schemas, and endpoints, refer to the README files in each service directory:

- [API Gateway](file:///Users/meena/.gemini/antigravity/scratch/PatientManagementSystem/api-gateway/README.md)
- [Auth Service](file:///Users/meena/.gemini/antigravity/scratch/PatientManagementSystem/auth-service/README.md)
- [Patient Service](file:///Users/meena/.gemini/antigravity/scratch/PatientManagementSystem/patient-service/README.md)
- [Billing Service](file:///Users/meena/.gemini/antigravity/scratch/PatientManagementSystem/billing-service/README.md)
- [Appointment Service](file:///Users/meena/.gemini/antigravity/scratch/PatientManagementSystem/appointment-service/README.md)
- [Analytics Service](file:///Users/meena/.gemini/antigravity/scratch/PatientManagementSystem/analytics-service/README.md)
- [Integration Tests](file:///Users/meena/.gemini/antigravity/scratch/PatientManagementSystem/integration-tests/README.md)
