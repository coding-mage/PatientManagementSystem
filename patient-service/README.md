# Patient Service

The Patient Service manages patient registration, profile directories, and coordinates downstream accounts.

## Port Configuration

- HTTP REST Port: `4000`

## Core Responsibilities

- **Patient Directory**: Stores demographics, date of birth, and email records. Enforces email uniqueness.
- **Billing Onboarding Link**: Interacts with the `billing-service` over gRPC. Upon successful patient registration, it calls the remote gRPC endpoint to establish an associated billing account.
- **Kafka Event Publisher**: Publishes change events (`PATIENT_CREATED`, `PATIENT_UPDATED`, `PATIENT_DELETED`) to the `patient` topic.

## API Endpoints

- `GET /patients`: Search patients by name or email.
- `GET /patients/{id}`: Fetch patient details by UUID.
- `POST /patients`: Register a new patient profile (triggers billing account creation and Kafka event).
- `PUT /patients/{id}`: Update patient details.
- `DELETE /patients/{id}`: Delete patient profile.

## Configuration Properties

- `billing.service.address`: Target hostname for the billing gRPC server.
- `billing.service.port`: Target port for the billing gRPC server.
- `spring.kafka.bootstrap-servers`: Bootstrap servers configuration for Kafka event publishing.
