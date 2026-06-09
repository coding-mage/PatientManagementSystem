# Billing Service

The Billing Service acts as the ledger of patient financial accounts. It tracks account balances, performs charge and credit adjustments, and exposes interfaces via HTTP REST and gRPC.

## Port Configuration

- HTTP REST Port: `4002`
- gRPC Server Port: `9095`

## Core Responsibilities

- **Balance Ledgers**: Manages monetary assets linked to patient UUID keys.
- **Credit & Charge Operations**: Modifies balances, ensuring proper database isolation levels during updates.
- **gRPC Interface**: Implements `BillingServiceGrpc` endpoints to support fast service-to-service creation and transaction requests.
- **Kafka Publisher**: Transmits event notifications (`BILLING_ACCOUNT_CREATED`, `BILLING_ACCOUNT_CREDITED`, etc.) to the `billing` and `billing-events` topics.

## Endpoints

### HTTP REST API
- `GET /billing-accounts`: Lists all accounts.
- `GET /billing-accounts/{id}`: Retrieves ledger status by account UUID.
- `GET /billing-accounts/patient/{patientId}`: Retrieves ledger by patient UUID.
- `POST /billing-accounts/{id}/charge`: Debits the account balance.
- `POST /billing-accounts/{id}/credit`: Credits the account balance.

### gRPC API (`billing.proto`)
- `createBillingAccount`: Provision an account.
- `chargeBillingAccount`: Perform transaction debit.
- `creditBillingAccount`: Perform transaction credit.
- `deleteBillingAccountByPatientId`: Clean up accounts.

## Configuration Properties

- `grpc.server.port`: Server port for incoming gRPC bindings.
- `spring.kafka.bootstrap-servers`: Address of the broker for Kafka publications.
