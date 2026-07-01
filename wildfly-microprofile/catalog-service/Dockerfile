FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn -pl catalog-service -am clean package -DskipTests

FROM quay.io/wildfly/wildfly:latest
USER root

# Download PostgreSQL JDBC driver
RUN curl -L https://jdbc.postgresql.org/download/postgresql-42.7.3.jar \
    -o /tmp/postgresql.jar

# Install it as a WildFly module
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --command=\
"module add \
  --name=org.postgresql \
  --resources=/tmp/postgresql.jar \
  --dependencies=javax.api,java.sql"

# Register the driver and add datasource during image build
RUN /opt/jboss/wildfly/bin/standalone.sh --admin-only & \
    until /opt/jboss/wildfly/bin/jboss-cli.sh --connect --commands="ls" > /dev/null 2>&1; do sleep 5; done && \
    /opt/jboss/wildfly/bin/jboss-cli.sh --connect --commands="\
      /subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql,driver-class-name=org.postgresql.Driver),\
      data-source add --name=CatalogDS --jndi-name=java:/CatalogDSP --driver-name=postgresql --connection-url=jdbc:postgresql://wf-catalog-db:5432/catalogdb --user-name=showdown --password=showdown123 --valid-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker --exception-sorter-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter --validate-on-match=true,\
      shutdown" && \
    wait

COPY --from=builder \
    /app/catalog-service/target/catalog-service.war \
    /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]

