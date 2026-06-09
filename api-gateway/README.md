# API Gateway

The API Gateway is built on Spring Cloud Gateway. It serves as the single public entry point for all client requests, routing them to downstream microservices and performing JWT signature verification and role-based access validation.

## Port Configuration

- Local Port: `4007`

## Routing Rules

| Path | Destination Service | Required Roles / Permissions |
| :--- | :--- | :--- |
| `/auth/**` | `auth-service` | Public |
| `/api/users/**` | `auth-service` | `ADMIN` |
| `/api/doctors/**` | `auth-service` | `ADMIN`, `PHYSICIAN` |
| `/api/patients/**` | `patient-service` | `ADMIN`, `RECEPTIONIST`, `PHYSICIAN` |
| `/api/billing/**` | `billing-service` (rewritten to `/billing-accounts/**`) | `ADMIN`, `RECEPTIONIST`, `PHYSICIAN`, `USER` |
| `/api/appointments/**` | `appointment-service` | `ADMIN`, `RECEPTIONIST`, `PHYSICIAN`, `USER` |
| `/api/analytics/**` | `analytics-service` (rewritten to `/analytics-events/**`) | `ADMIN` |

## Filters and Claims Propagation

The gateway implements a custom GatewayFilter `JwtValidationGatewayFilterFactory` that performs validation on requests containing a `Bearer` token in the `Authorization` header:

1. Submits token validate requests to `auth-service` via WebClient.
2. Checks that the decoded user role matches the permitted configuration.
3. Propagates identity metadata downstream by injecting headers:
   - `X-User-Role`
   - `X-User-Email`

## Environment Variables

- `AUTH_SERVICE_URL`: Base URL of the authentication service (e.g., `http://auth-service:4005`).
