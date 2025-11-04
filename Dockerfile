# syntax=docker/dockerfile:1.6
FROM maven:3.8.4-eclipse-temurin-17-alpine AS builder

WORKDIR /workspace

COPY pom.xml .
 
# Leverage BuildKit cache for Maven repo
RUN --mount=type=cache,target=/root/.m2 mvn -B -q -T 1C dependency:go-offline

COPY src ./src
 
# Use cached Maven repo and quiet, parallel build
RUN --mount=type=cache,target=/root/.m2 mvn -B -q -T 1C -DskipTests clean package

FROM eclipse-temurin:17-jre-alpine
ENV SPRING_PROFILES_ACTIVE=data-import

COPY --from=builder /workspace/target/*.war  /Marginal-tax-rate-calculator-0.0.1-SNAPSHOT.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/Marginal-tax-rate-calculator-0.0.1-SNAPSHOT.war"]
