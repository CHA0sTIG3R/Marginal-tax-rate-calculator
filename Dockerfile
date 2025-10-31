FROM maven:3.8.4-eclipse-temurin-17-alpine AS builder

WORKDIR /workspace

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

# TODO: Temporary default for testing: force data-import profile.
#       Best practice is to remove this line and set profiles via deploy config
#       (e.g., env var SPRING_PROFILES_ACTIVE or --spring.profiles.active).
#       For the initial one-off import, run the same image with:
#       docker run --rm -e SPRING_PROFILES_ACTIVE=data-import your-image:tag
ENV SPRING_PROFILES_ACTIVE=data-import

COPY --from=builder /workspace/target/*.war  /Marginal-tax-rate-calculator-0.0.1-SNAPSHOT.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/Marginal-tax-rate-calculator-0.0.1-SNAPSHOT.war"]
