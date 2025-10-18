# 1. Imagen base con Maven y JDK 17
FROM maven:3.9.2-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

RUN ./mvnw clean package -DskipTests

# Imagen final más ligera
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Usa variable de entorno para el perfil
CMD sh -c "echo DB_URL=$DB_URL && echo DB_USERNAME=$DB_USERNAME && echo DB_PASSWORD=<hidden> && java -jar app.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE"
