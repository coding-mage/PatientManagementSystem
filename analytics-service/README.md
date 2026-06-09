# Analytics Service

The Analytics Service is an event-driven logger that collects metrics and lifecycle audits from the messaging pipeline.

## Port Configuration

- HTTP REST Port: `4004`

## Core Responsibilities

- **Kafka Event Consumer**: Subscribes to multiple topics:
  - `patient` (deserializes Protobuf `PatientEvent`)
  - `appointment` (deserializes Protobuf `AppointmentEvent`)
  - `billing` (parses JSON metadata fields)
  - `patient-events` (evaluates headers for tracing correlation IDs)
- **Metrics Database**: Persists all incoming events to a relational database for archiving.
- **Aggregated Analytics**: Exposes endpoints to check logs and summaries.

## API Endpoints

- `GET /analytics-events`: Lists all stored audit logs.
- `GET /analytics-events/{id}`: Fetches a single event log by UUID.
- `GET /analytics-events/summary`: Retrieves system analytics counters, grouping event counts by event type.

## Configuration Properties

- `spring.kafka.bootstrap-servers`: Address of the broker for Kafka subscriptions.
