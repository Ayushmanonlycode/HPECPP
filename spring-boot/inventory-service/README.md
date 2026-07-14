# Inventory Service — Spring Boot

The **Inventory Service** manages pet store inventory, tracking the physical stock levels, processing reservations for pending checkouts, and facilitating stock release/adjustments.

---

## Technology Stack

- **Core Framework**: Spring Boot 3 (Java 21)
- **Database Access**: Spring Data JPA & Hibernate
- **Database**: PostgreSQL (backed by `sb-inventory-db` container)
- **Security**: Spring Security OAuth2 Resource Server (Keycloak public key validation)

---

## Database Schema (`inventory_items` Table)

Backed by PostgreSQL. The table mapping defined in `InventoryItem.java` is:

| Column | Data Type | Constraints | Description |
|---|---|---|---|
| `id` | `UUID` | Primary Key (auto-generated) | Unique inventory entry identifier. |
| `item_sku` | `VARCHAR` | Unique, Not Null | SKU matching item entries in the Catalog database. |
| `quantity` | `INTEGER` | Not Null | Total physical stock quantity in the warehouse. |
| `reserved_quantity`| `INTEGER` | Not Null, Default: `0` | Quantity reserved for pending/uncompleted orders. |
| `last_updated` | `TIMESTAMP`| Not Null | Automatically updated timestamp of last edit. |

### Helper Method
The model provides the getter `getAvailableQuantity()` which returns `quantity - reservedQuantity`. This represents the net stock available for new purchases.

---

## Security & Access Control

The inventory service operates entirely behind the service boundary:
- **Actuator Endpoint**: `/actuator/**` is public for Prometheus and health probes.
- **All Other Endpoints**: Require a valid Bearer JWT issued by Keycloak. This prevents unauthorized direct stock manipulation, enforcing that stock reservations and adjustments must go through authorized channels like the `catalog-service` or `order-capture-service` using the Client Credentials flow.

---

## API Endpoints

- **`GET /api/inventory`**: List stock details for all SKUs.
- **`GET /api/inventory/{sku}`**: Get stock details for a specific SKU.
- **`POST /api/inventory`**: Initialize stock entry for a SKU.
  - **Body**: `StockAdjustmentDto` (sku, quantity)
- **`PUT /api/inventory/{sku}/adjust`**: Increments or decrements total stock quantity.
  - **Body**: `{"quantity": 10}` (accepts positive/negative integers)
- **`POST /api/inventory/{sku}/reserve`**: Reserves a quantity of stock for checkout.
  - **Body**: `{"quantity": 1}` (increases `reserved_quantity` and decreases available stock)
- **`POST /api/inventory/{sku}/release`**: Releases a previously reserved quantity.
  - **Body**: `{"quantity": 1}` (decreases `reserved_quantity` without changing total physical quantity)
- **`GET /api/inventory/{sku}/availability?quantity={quantity}`**: Check if there is enough available stock.
  - **Returns**: `{"sku": "EST-1", "quantity": 1, "available": true}`

---

## Environment Variables & Configuration

Declared in `src/main/resources/application.properties`:

| Property Path | Environment Variable | Default Value | Description |
|---|---|---|---|
| `spring.datasource.url` | `SPRING_DATASOURCE_URL` | `jdbc:postgresql://sb-inventory-db:5432/inventorydb` | JDBC target connection URL. |
| `spring.security.oauth2.resourceserver.jwt.issuer-uri` | `KEYCLOAK_URL` | `http://localhost:8180/realms/jpetstore` | Keycloak server URL for resource validation. |
