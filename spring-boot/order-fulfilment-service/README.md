# Order Fulfilment Service — Spring Boot

The **Order Fulfilment Service** manages the shipping, tracking, and delivery lifecycle of captured JPetStore orders.

---

## Technology Stack

- **Core Framework**: Spring Boot 3 (Java 21)
- **Database Access**: Spring Data JPA & Hibernate
- **Database**: PostgreSQL (backed by `sb-order-fulfillment-db` container)
- **Security**: Spring Security OAuth2 Resource Server (Keycloak public key validation)

---

## Lifecycle & Status Transitions

The service transitions order shipments through the following states defined in `FulfilmentStatus.java`:

```
 [ PENDING ] ──────► [ PROCESSING ] ──────► [ SHIPPED ] ──────► [ DELIVERED ]
      │                                         │
      └─────────────────────────────────────────┴────────────► [ FAILED ]
```

- **PENDING**: Fulfilment record initiated by Order Capture Service checkout.
- **PROCESSING**: Items are being packed in the warehouse.
- **SHIPPED**: Order has been handed over to the carrier (tracking number and carrier recorded).
- **DELIVERED**: Order has reached the destination shipping address.
- **FAILED**: Shipment has encountered a terminal failure.

---

## Database Schema (`fulfilments` Table)

Backed by PostgreSQL. The table mapping defined in `Fulfilment.java` is:

| Column | Data Type | Constraints | Description |
|---|---|---|---|
| `id` | `UUID` | Primary Key (auto-generated) | Unique fulfilment tracking identifier. |
| `order_id` | `VARCHAR(20)` | Not Null | Foreign ID pointing to the order in Order Capture database. |
| `status` | `VARCHAR` | Not Null, Default: `"PENDING"` | Current shipment state. |
| `tracking_number`| `VARCHAR` | Nullable | Package tracking reference code. |
| `carrier` | `VARCHAR` | Nullable | Name of shipping company (e.g. Fedex, USPS). |
| `shipped_at` | `TIMESTAMP` | Nullable | Timestamp when package was marked shipped. |
| `delivered_at` | `TIMESTAMP` | Nullable | Timestamp when package was marked delivered. |
| `created_at` | `TIMESTAMP` | Not Null | Creation timestamp. |
| `updated_at` | `TIMESTAMP` | Not Null | Update timestamp. |

---

## Security Logic

The service functions behind the cluster gateway:
- **Metrics/Actuator**: Public at `/actuator/**` for telemetry collection.
- **Business Endpoints**: Restricted to validated JWT Bearer tokens issued by Keycloak. Secure requests from internal services (such as `order-capture-service` routing checkouts) authenticate using the Keycloak Client Credentials flow.

---

## API Endpoints

- **`POST /api/fulfilments`**: Create a new fulfilment record for an order.
  - **Body**: `CreateFulfilmentDto` (orderId)
  - **Returns**: `201 Created` with `FulfilmentDto`
- **`GET /api/fulfilments`**: List all fulfilment records.
- **`GET /api/fulfilments/{id}`**: Get fulfilment details by ID.
- **`GET /api/fulfilments/order/{orderId}`**: Get fulfilment details for a specific order.
- **`PUT /api/fulfilments/{id}/process`**: Move status to `PROCESSING`.
- **`PUT /api/fulfilments/{id}/ship`**: Move status to `SHIPPED`.
- **`PUT /api/fulfilments/{id}/deliver`**: Move status to `DELIVERED`.
- **`PUT /api/fulfilments/{id}/fail`**: Move status to `FAILED`.

---

## Environment Variables & Configuration

Declared in `src/main/resources/application.properties`:

| Property Path | Environment Variable | Default Value | Description |
|---|---|---|---|
| `spring.datasource.url` | `SPRING_DATASOURCE_URL` | `jdbc:postgresql://sb-order-fulfilment-db:5432/orderfulfilmentdb` | JDBC connection URL. |
| `spring.security.oauth2.resourceserver.jwt.issuer-uri` | `KEYCLOAK_URL` | `http://localhost:8180/realms/jpetstore` | Keycloak issuer endpoint. |
