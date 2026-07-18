# User Service — WildFly MicroProfile

The **User Service** manages user accounts, registration, and login for the WildFly stack. It exposes Jakarta JAX-RS endpoints for CRUD operations on user profiles.

---

## Technology Stack

- **Runtime**: WildFly Application Server (latest)
- **Language**: Java 17
- **Framework**: Jakarta EE 10, MicroProfile 6.1
- **Database Access**: Jakarta Persistence (JPA) with Hibernate
- **Database**: PostgreSQL 16 (backed by `wf-user-db` container)
- **Packaging**: WAR (`user-service.war`)

---

## Database Schema (`Users` Table)

| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | `VARCHAR` | Primary Key (manually assigned) | User identifier. |
| `username` | `VARCHAR` | — | Login username. |
| `email` | `VARCHAR` | Unique | User email address. |
| `password` | `VARCHAR` | — | Password stored in **plaintext**. |
| `first_name` | `VARCHAR` | — | First name. |
| `last_name` | `VARCHAR` | — | Last name. |
| `phone` | `VARCHAR` | — | Telephone number. |
| `address` | `VARCHAR` | — | Street address. |
| `city` | `VARCHAR` | — | City. |
| `state` | `VARCHAR` | — | State. |
| `zip` | `VARCHAR` | — | Postal code. |
| `country` | `VARCHAR` | — | Country. |
| `status` | `VARCHAR` | — | Account status. |
| `created_at` | `TIMESTAMP` | — | Creation timestamp. |
| `updated_at` | `TIMESTAMP` | — | Last modification timestamp. |

---

## Security

> **Warning**: This service stores and validates passwords in **plaintext**. The `UserRepository` performs login verification via a JPA query that directly compares the `username` and `password` fields without hashing. There is no BCrypt encoding, no JWT generation, no token-based session management, and no security annotations on any endpoint.

---

## API Endpoints

All endpoints are exposed under the WAR context root `/user-service/users`.

| Method | Path | Description |
|---|---|---|
| `POST` | `/users/register` | Register a new user. |
| `POST` | `/users/login` | Login (plaintext password comparison). |
| `GET` | `/users` | List all users. |
| `GET` | `/users/{id}` | Get user profile by ID. |
| `PUT` | `/users/{id}` | Update user profile. |
| `DELETE` | `/users/{id}` | Delete a user. |

---

## Datasource Configuration

The PostgreSQL datasource is registered during the Docker image build via WildFly CLI commands in the `Dockerfile`:

```
data-source add --name=UserServiceDS --jndi-name=java:/UserDSP \
  --connection-url=jdbc:postgresql://wf-user-db:5432/userdb \
  --user-name=showdown --password=showdown123
```

> **Warning**: The database credentials (`showdown` / `showdown123`) are hardcoded into the Docker image at build time. They do not reference the `DB_USER` and `DB_PASSWORD` environment variables defined in `docker-compose.yml`. Changing database credentials requires rebuilding the image.

---

## Docker Configuration

| Property | Value |
|---|---|
| Container Name | `wf-user-service` |
| Internal Port | `8080` |
| Mapped Host Port | `9081` |
| Database Container | `wf-user-db` |
| Database Name | `userdb` |
