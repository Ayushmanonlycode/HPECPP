# Microservices Showdown

Head-to-head comparison: **Spring Boot 3** vs **WildFly MicroProfile 6**.

## Structure

```
microservices-showdown/
├── spring-boot/                  # Stack A
│   ├── api-gateway/
│   ├── user-service/
│   ├── catalog-service/
│   ├── inventory-service/
│   ├── order-capture-service/
│   ├── order-fulfilment-service/
│   └── cart-service/
├── wildfly-microprofile/         # Stack B
│   ├── api-gateway/
│   ├── user-service/
│   ├── catalog-service/
│   ├── inventory-service/
│   ├── order-capture-service/
│   ├── order-fulfilment-service/
│   └── cart-service/
├── observability/
│   ├── prometheus/
│   ├── grafana/
│   └── otel-collector/
├── docker-compose.yml
└── README.md
```

## Quick Start

```bash
# Start observability stack
docker compose --profile observability up -d

# Start Spring Boot stack
docker compose --profile spring up -d

# Start WildFly stack
docker compose --profile wildfly up -d
```

## Service Ports

| Service | Spring Boot | WildFly |
|---|---|---|
| API Gateway | 8080 | 9080 |
| User Service | 8081 | 9081 |
| Catalog Service | 8082 | 9082 |
| Inventory Service | 8083 | 9083 |
| Order Capture | 8084 | 9084 |
| Order Fulfilment | 8085 | 9085 |
| Cart Service | 8086 | 9086 |

## Observability

| Tool | URL |
|---|---|
| Grafana | http://localhost:3000 |
| Prometheus | http://localhost:9090 |
| Jaeger | http://localhost:16686 |
