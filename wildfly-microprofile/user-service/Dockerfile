FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn -pl user-service -am clean package -DskipTests

FROM quay.io/wildfly/wildfly:latest
USER root

RUN curl -L https://jdbc.postgresql.org/download/postgresql-42.7.3.jar \
    -o /tmp/postgresql.jar

RUN /opt/jboss/wildfly/bin/jboss-cli.sh --command=\
"module add \
  --name=org.postgresql \
  --resources=/tmp/postgresql.jar \
  --dependencies=javax.api,java.sql"

RUN /opt/jboss/wildfly/bin/standalone.sh --admin-only & \
    sleep 10 && \
    /opt/jboss/wildfly/bin/jboss-cli.sh --connect --commands="\
      /subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql,driver-class-name=org.postgresql.Driver),\
      data-source add --name=UserServiceDS --jndi-name=java:/UserDSP --driver-name=postgresql --connection-url=jdbc:postgresql://wf-user-db:5432/userdb --user-name=showdown --password=showdown123 --valid-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker --exception-sorter-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter --validate-on-match=true,\
      shutdown" && \
    wait

COPY --from=builder \
    /app/user-service/target/user-service.war \
    /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]