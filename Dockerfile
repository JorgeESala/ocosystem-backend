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
# ... tus instrucciones anteriores

# WORKDIR y copia final del JAR
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# CMD temporal para debug
CMD sh -c "echo SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE && \
           echo DB_URL=$DB_URL && \
           echo DB_USERNAME=$DB_USERNAME && \
           echo DB_PASSWORD=$DB_PASSWORD && \
           java -jar app.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE"
