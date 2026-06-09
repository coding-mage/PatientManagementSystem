# Authentication Service

The Authentication Service handles user credentials, issues signed JSON Web Tokens (JWT), and exposes endpoints for credential verification and role assignment. It also exposes a gRPC interface used by other microservices to verify user profiles.

## Port Configuration

- HTTP REST Port: `4005`
- gRPC Server Port: `9097`

## Core Responsibilities

- **User Accounts**: Manages user identities and password encryption using BCrypt.
- **Token Generation**: Generates JWTs containing user emails and roles, signed with a HMAC-SHA algorithm.
- **gRPC UserService**: Exposes a `UserServiceGrpc` service definition for high-speed synchronous validation.

## API Endpoints

### User Management
- `POST /users`: Registers a new administrative or receptionist user.
- `POST /login`: Authenticates credentials and returns a JWT token.
- `GET /validate`: Decodes a token passed in the Authorization header and returns role headers (`X-User-Role`, `X-User-Email`).

### Doctor Management
- `POST /doctors`: Onboards new physician profiles and links them to user credential models.

## Configuration Properties

- `jwt.secret`: Signature key for encoding and decoding tokens.
- `grpc.server.port`: Target port for gRPC server binding (Default: `9097`).
