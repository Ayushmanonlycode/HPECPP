FROM maven:3.9.7-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -pl order-capture-service -am -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/order-capture-service/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
