
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy JAR with correct ownership in one layer
COPY --chown=10001:0 --from=build /app/target/*.jar app.jar

# Create log directory with correct ownership
RUN mkdir -p /app/logs && chown 10001:0 /app/logs

USER 10001

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]