# Load Testing — JMeter

## Prerequisites
- JMeter 5.6.3+: https://jmeter.apache.org/download_jmeter.cgi
- Java 21+
- One backend stack running (either Spring Boot OR WildFly, not both)
- Observability stack running: `docker compose --profile observability up -d`

## Running Against Spring Boot (Port 8080)

```bash
# 1. Start the Spring Boot stack
docker compose --profile spring up -d

# 2. Wait for all services to be healthy (~60s)
docker ps --format "table {{.Names}}\t{{.Status}}"

# 3. Run the load test (50 concurrent users, 300s duration)
jmeter -n -t jmeter/jpetstore-load-test.jmx \
  -JBASE_URL=http://localhost:8080 \
  -JRAMP_UP_SECONDS=30 \
  -JDURATION_SECONDS=300 \
  -l results/spring-boot-50users.jtl \
  -e -o results/spring-boot-50users-report/
```

## Running Against WildFly (Port 9080)

```bash
# 1. Stop Spring Boot stack first (only ONE stack at a time for clean measurements)
docker compose --profile spring down

# 2. Start the WildFly stack
docker compose --profile wildfly up -d

# 3. Run the IDENTICAL test plan — only BASE_URL changes
jmeter -n -t jmeter/jpetstore-load-test.jmx \
  -JBASE_URL=http://localhost:9080 \
  -JRAMP_UP_SECONDS=30 \
  -JDURATION_SECONDS=300 \
  -l results/wildfly-50users.jtl \
  -e -o results/wildfly-50users-report/
```

## Load Levels (Run each separately)

| Concurrent Users | Ramp-Up | Duration | Output File |
|---|---|---|---|
| 50 | 30s | 300s (5 min) | `*-50users.jtl` |
| 100 | 60s | 300s (5 min) | `*-100users.jtl` |
| 200 | 90s | 300s (5 min) | `*-200users.jtl` |
| 200 | 90s | 1800s (30 min soak) | `*-soak.jtl` |

Override thread count:
```bash
jmeter -n -t jmeter/jpetstore-load-test.jmx \
  -JBASE_URL=http://localhost:8080 \
  -JThreadGroup.num_threads=100 \
  -JRAMP_UP_SECONDS=60 \
  -JDURATION_SECONDS=300 \
  -l results/spring-boot-100users.jtl
```

## Key Metrics to Capture (from results CSV)

Per Kamala's direction and the evaluation rubric:

| Metric | Target Column in CSV | Scoring Threshold |
|---|---|---|
| P50 response time | Use Aggregate Report | 5=<50ms |
| P95 response time | Use Aggregate Report | 3=100–200ms |
| P99 response time | Use Aggregate Report | 1=>500ms |
| Throughput (req/s) | Use Summary Report | 5=>500 req/s |
| Error rate | `success` column | 5=<1%, 1=>5% |

## Important Notes
- Run only ONE stack at a time (Kamala's requirement for "clean measurements")
- Ensure no other processes are consuming significant CPU/RAM during the test
- Capture `docker stats` output during the test for resource consumption metrics
- Jaeger traces will automatically capture per-service hop latency
