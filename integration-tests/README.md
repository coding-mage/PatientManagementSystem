# Integration Tests

This module contains end-to-end integration tests that validate security, role permissions, patient scheduling conflict logic, and cross-service actions.

## Port and Host Target

The integration tests run against the active system containers (routing through the API Gateway at `http://localhost:4007`). Ensure the system containers are running prior to executing tests.

## Test Areas

- **AuthIntegrationTest**: Validates user credential creation, secure logins, invalid access controls, and claims validation.
- **PatientIntegrationTest**: Validates patient onboarding flows, demography validations, and gRPC client integrations.
- **DoctorAndConflictIntegrationTest**: Validates calendar scheduling constraints, conflict detection algorithms, and appointment bookings.

## Running Tests

From the `integration-tests` directory, run:

```bash
mvn test
```
