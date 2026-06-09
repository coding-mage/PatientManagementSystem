# Appointment Service

The Appointment Service handles the creation, update, deletion, and status tracking of appointments. It performs calendar conflict checks and coordinates financial actions downstream.

## Port Configuration

- HTTP REST Port: `4006`

## Core Responsibilities

- **Appointment Scheduling**: Stores appointment times, durations, notes, and doctor/patient linkages.
- **Overlapping Conflict Check**: Computes availability mathematically. An overlap is flagged if:
  $$\text{newStart} < \text{existingEnd} \quad \land \quad \text{existingStart} < \text{newEnd}$$
  The validation runs against both the target patient's and target physician's schedules, throwing a `400 Bad Request` on conflict.
- **Microservice Integrations**:
  - Validates patient profile existence using REST client (`patient.service.url`).
  - Checks doctor account credentials via gRPC client (`auth.service.address`).
  - Charges/credits the patient's account in `billing-service` using gRPC (`billing.service.address`).
- **Kafka Publisher**: Emits change events (`APPOINTMENT_CREATED`, `APPOINTMENT_UPDATED`, `APPOINTMENT_DELETED`) to the `appointment` topic.

## API Endpoints

- `GET /appointments`: Retrieve appointments with filtering options (`patientId`, `userId`, `date`).
- `GET /appointments/{id}`: Fetch appointment details by UUID.
- `POST /appointments`: Create a new scheduled appointment.
- `PUT /appointments/{id}`: Reschedule, edit notes, or adjust fees.
- `DELETE /appointments/{id}`: Cancel/delete an appointment.

## Configuration Properties

- `patient.service.url`: Target URL of the patient microservice.
- `auth.service.address` / `auth.service.port`: Host and port for gRPC validation client.
- `billing.service.address` / `billing.service.port`: Host and port for gRPC transaction client.
