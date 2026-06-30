# Downloads the OpenTelemetry Java Agent and makes it available as a Docker volume.
# All Spring Boot and WildFly containers mount this volume at /otel-agent/.
FROM curlimages/curl:8.7.1 AS downloader
WORKDIR /download
RUN curl -fsSL -o opentelemetry-javaagent.jar \
    https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.4.0/opentelemetry-javaagent.jar

# Use alpine (not scratch) so /bin/sh is available for the docker-compose entrypoint.
FROM alpine:3.20
COPY --from=downloader /download/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar
