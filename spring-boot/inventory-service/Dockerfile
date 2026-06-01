FROM maven:3.9.7-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -pl inventory-service -am -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/inventory-service/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
