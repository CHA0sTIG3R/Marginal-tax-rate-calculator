FROM maven:3.8.4-eclipse-temurin-17-alpine AS builder

WORKDIR /workspace

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

ENV SPRING_PROFILES_ACTIVE=data-import

COPY --from=builder /workspace/target/*.war  /Marginal-tax-rate-calculator-0.0.1-SNAPSHOT.war

EXPOSE 8080

# TODO: Consider not forcing data-import profile in ENTRYPOINT. Instead, let
#  docker-compose or environment variables set the desired profile.
#  Example override at run-time:
#  - command: ["--spring.profiles.active="]
#  Or set TAX_IMPORT_ON_STARTUP=false (default) to skip import in data-import profile.
ENTRYPOINT ["java", "-jar", "/Marginal-tax-rate-calculator-0.0.1-SNAPSHOT.war", "--spring.profiles.active=data-import"]
