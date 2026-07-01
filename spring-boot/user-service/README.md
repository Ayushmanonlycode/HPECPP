# User Service — Spring Boot

The **User Service** manages user accounts, registration, validation, profile management, and authentication inside the Spring Boot cluster.

Unlike other domain services in this stack that validate incoming Bearer tokens using Keycloak's public keys, the User Service relies on **custom local JWT generation and verification** using a symmetric key signature (HMAC-SHA256).

---

## Technology Stack

- **Core Framework**: Spring Boot 3 (Java 21)
- **Database Access**: Spring Data JPA & Hibernate
- **Database**: PostgreSQL (backed by `sb-user-db` container)
- **Security**: Spring Security (with custom filter chain and `BCryptPasswordEncoder`)
- **JSON Web Tokens**: `io.jsonwebtoken` (JJWT)

---

## Database Schema (`users` Table)

Backed by PostgreSQL. The table mapping defined in `User.java` is:

| Column | Data Type | Constraints | Description |
|---|---|---|---|
| `id` | `UUID` | Primary Key (auto-generated) | Unique user identifier. |
| `username` | `VARCHAR` | Unique, Not Null | Unique username for login. |
| `email` | `VARCHAR` | Unique, Not Null | Email address. |
| `password` | `VARCHAR` | Not Null | Hashed password (BCrypt). |
| `first_name` | `VARCHAR` | Nullable | User's first name. |
| `last_name` | `VARCHAR` | Nullable | User's last name. |
| `phone` | `VARCHAR` | Nullable | Telephone number. |
| `address` | `VARCHAR` | Nullable | Street address. |
| `city` | `VARCHAR` | Nullable | City of residence. |
| `state` | `VARCHAR` | Nullable | State. |
| `zip` | `VARCHAR` | Nullable | ZIP / Postal code. |
| `country` | `VARCHAR` | Nullable | Country of residence. |
| `status` | `VARCHAR` | Not Null, Default: `"ACTIVE"`| User account status. |
| `created_at` | `TIMESTAMP`| Not Null, Updatable: False | Creation timestamp. |
| `updated_at` | `TIMESTAMP`| Not Null | Last modification timestamp. |

---

## Security & Authentication Pipeline

1. **Password Hashing**: Passwords are encrypted before database insertion using a custom `BCryptPasswordEncoder` bean.
2. **Local Token Issuance**:
   - On successful credentials validation via `/api/users/login`, the service generates a custom JWT signed via `Jwts.builder()` using the local symmetric key config (`jwt.secret`).
3. **Local Token Verification**:
   - Secured endpoints trigger the `JwtAuthenticationFilter`, which extracts the Bearer token, extracts the subject (username), loads the user details from the database, and validates token expiration.
4. **Endpoint Permissions**:
   - `POST /api/users/register`: Public
   - `POST /api/users/login`: Public
   - `GET /api/users/**`: Public (for user verification by other microservices)
   - Other requests (PUT, DELETE): Authenticated (requiring custom JWT Bearer token)

---

## API Endpoints

### Public Endpoints

*   **`POST /api/users/register`**: Creates a new user profile.
    *   **Body**: `UserRegistrationDto` (username, email, password, profile details)
    *   **Returns**: `201 Created` with `UserProfileDto`
*   **`POST /api/users/login`**: Validates credentials.
    *   **Body**: `UserLoginDto` (username, password)
    *   **Returns**: `200 OK` with `AuthResponseDto` (contains token, username, and UUID)

### Secure Endpoints (Require Bearer JWT)

*   **`GET /api/users`**: List all users.
*   **`GET /api/users/{id}`**: Get user profile details by ID.
*   **`PUT /api/users/{id}`**: Full update of user registration profile.
*   **`PUT /api/users/{id}/profile`**: Partial update of user profile details (first name, address, phone, etc.).
*   **`DELETE /api/users/{id}`**: Delete user account.

---

## Environment Variables & Configuration

The parameters are declared in `src/main/resources/application.properties`:

| Property Path | Environment Variable | Default Value | Description |
|---|---|---|---|
| `spring.datasource.url` | `SPRING_DATASOURCE_URL` | `jdbc:postgresql://sb-user-db:5432/userdb` | JDBC target connection URL. |
| `jwt.secret` | `JWT_SECRET` | (32-character key) | Symmetric secret key used to sign and verify custom JWTs. |
| `jwt.expiration-ms` | `JWT_EXPIRATION_MS` | `86400000` (24h) | JWT validity period in milliseconds. |
