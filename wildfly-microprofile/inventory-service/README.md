# Inventory Service — WildFly MicroProfile

The **Inventory Service** tracks warehouse stock levels for the WildFly stack. It provides a simple CRUD interface for managing inventory quantities, without the reservation or availability-checking semantics found in the Spring Boot equivalent.

---

## Technology Stack

- **Runtime**: WildFly Application Server (latest)
- **Language**: Java 17
- **Framework**: Jakarta EE 10, MicroProfile 6.1
- **Database Access**: Jakarta Persistence (JPA) with Hibernate
- **Database**: PostgreSQL 16 (backed by `wf-inventory-db` container)
- **Packaging**: WAR (`inventory-service.war`)

---

## Database Schema (`Inventory` Entity)

| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | `VARCHAR` | Primary Key | Inventory record identifier. |
| `item_id` | `VARCHAR` | — | Reference to the catalog item SKU. |
| `quantity` | `INTEGER` | — | Current stock quantity. |

Note the absence of `reserved_quantity` and `last_updated` fields that exist in the Spring Boot counterpart. This service tracks only raw stock levels with no reservation support.

---

## Key Architectural Differences from Spring Boot

| Feature | Spring Boot | WildFly |
|---|---|---|
| Stock Reservation | `POST /{sku}/reserve` and `POST /{sku}/release` | Not implemented |
| Availability Check | `GET /{sku}/availability?quantity=N` | Not implemented |
| Reserved Quantity Tracking | `reserved_quantity` column with `getAvailableQuantity()` | Not implemented |
| Security | OAuth2 JWT validation on all endpoints | None |

---

## API Endpoints

All endpoints are exposed under the WAR context root `/inventory-service`.

| Method | Path | Description |
|---|---|---|
| `GET` | `/inventory/all` | List all inventory records. |
| `GET` | `/inventory/item?itemId={id}` | Get inventory by item ID. |
| `PUT` | `/inventory/update?itemId={id}&quantity={qty}` | Update stock quantity for an item. |
| `POST` | `/inventory/add` | Add a new inventory record. Body: `Inventory` JSON. |

---

## Security

No security validation is implemented. All endpoints are publicly accessible without authentication.

---

## Docker Configuration

| Property | Value |
|---|---|
| Container Name | `wf-inventory-service` |
| Internal Port | `8080` |
| Mapped Host Port | `9083` |
| Database Container | `wf-inventory-db` |
| Database Name | `inventorydb` |
