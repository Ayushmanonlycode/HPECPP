FROM maven:3.9.7-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -pl order-fulfilment-service -am -DskipTests -q

FROM quay.io/wildfly/wildfly:34.0.1.Final-jdk21
COPY --from=build /app/order-fulfilment-service/target/*.war $JBOSS_HOME/standalone/deployments/
EXPOSE 8080 9990
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
