FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn -pl cart-service -am clean package -DskipTests

FROM quay.io/wildfly/wildfly:latest
USER root

COPY --from=builder \
    /app/cart-service/target/cart-service.war \
    /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]