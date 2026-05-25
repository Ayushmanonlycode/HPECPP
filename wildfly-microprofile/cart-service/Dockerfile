FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests -q

FROM quay.io/wildfly/wildfly:31.0.1.Final-jdk21
COPY --from=build /app/target/*.war $JBOSS_HOME/standalone/deployments/
EXPOSE 8080 9990
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
