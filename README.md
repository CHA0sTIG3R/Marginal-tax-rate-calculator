# Marginal Tax Rate Calculator

> **Spring Boot REST API** serving U.S. federal income tax bracket data (1862–present) and computation endpoints.

## Overview

This application exposes historical tax brackets and metrics, and calculates liabilities for specified income scenarios. It pairs with the [`tax_bracket_ingest`](https://github.com/CHA0sTIG3R/tax-bracket-ingest) microservice, which scrapes each year’s IRS data, archives it to S3, and pushes new records here.

## Implementation Status

| Component / Endpoint             | Status        |
|----------------------------------|---------------|
| Historical data import (S3 CSV)  | ✅ Implemented |
| `POST /api/v1/tax/upload`        | ✅ Implemented |
| `GET  /api/v1/tax/years`         | ✅ Implemented |
| `GET  /api/v1/tax/filing-status` | ✅ Implemented |
| `GET  /api/v1/tax/rate`          | ✅ Implemented |
| `POST /api/v1/tax/breakdown`     | ✅ Implemented |
| `GET  /api/v1/tax/summary`       | ✅ Implemented |
| `GET  /api/v1/tax/history`       | ✅ Implemented |
| `POST /api/v1/tax/simulate`      | ✅ Implemented |
| Swagger UI / OpenAPI docs        | ✅ Implemented |
| Spring Boot Actuator endpoints   | ✅ Implemented |
| Docker container (WAR)           | ✅ Implemented |
| OAuth2 / Security                | 🔲 Planned    |
| Rate limiting / Throttling       | 🔲 Planned    |

> ✅ = Complete & tested   🔲 = Not yet implemented

---

## Features

* **REST Endpoints** covering data ingestion, years, filing statuses, brackets, breakdowns, summaries, historical metrics, and bulk simulations.
* **Data Import** on startup from an S3 CSV archive (`TaxDataBootstrapper`).
* **Spring Data JPA** with PostgreSQL (default) or H2 in-memory.
* **OpenAPI / Swagger UI** for interactive API docs.
* **Spring Boot Actuator** for health, metrics, and info endpoints.
* **Unit & Integration Tests** using JUnit 5 and Testcontainers.

---

## Technology Stack

| Layer       | Technology                        |
|-------------|-----------------------------------|
| Language    | Java 17                           |
| Framework   | Spring Boot 3.4.4                 |
| Build tool  | Maven (WAR packaging)             |
| Persistence | Spring Data JPA + PostgreSQL / H2 |
| CSV Parsing | OpenCSV                           |
| API Docs    | springdoc-openapi (Swagger UI)    |
| Testing     | JUnit 5, Testcontainers, Mockito  |
| Actuator    | spring-boot-starter-actuator      |
| AWS SDK     | software.amazon.awssdk\:s3        |

---

## Getting Started

### Prerequisites

* **JDK 17** or newer
* **Maven 3.8+**
* **PostgreSQL 12+** (or use embedded H2)

### Clone & Run

```bash
git clone https://github.com/CHA0sTIG3R/Marginal-tax-rate-calculator.git
cd Marginal-tax-rate-calculator
```

#### With Data Import

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=data-import
```

#### API Only

```bash
mvn spring-boot:run
```

Service listens on port **8080**.

---

## Configuration

This project ships a tracked `src/main/resources/application.properties` that contains no secrets and defers to environment variables. Configure via `.env`, environment vars, or by overriding the properties file.

Recommended: copy `.env.example` to `.env` and set real values.

Key settings and env mappings:

```properties
# JPA schema management
spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO:update}

# Ingestion API key for /api/v1/tax/upload (required to enable uploads)
app.ingest.api-key=${APP_INGEST_API_KEY:}

# Data import (profile: data-import)
tax.import-on-startup=${TAX_IMPORT_ON_STARTUP:false}
tax.s3-bucket=${TAX_S3_BUCKET:}
tax.s3-key=${TAX_S3_KEY:}

# Database (PostgreSQL example)
spring.datasource.url=${SPRING_DATASOURCE_URL:}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:}
spring.datasource.driver-class-name=${SPRING_DATASOURCE_DRIVER:}
```

Notes:
- When `APP_INGEST_API_KEY` is unset or empty, uploads to `/api/v1/tax/upload` are rejected (401), but the app still starts.
- For Docker Compose, variables in `.env` are loaded automatically (see `env_file: .env`).

---

## API Reference

Base path: `/api/v1/tax`

| Endpoint                                                               | Method | Description                                                                                        |
|------------------------------------------------------------------------|--------|----------------------------------------------------------------------------------------------------|
| `/upload`                                                              | POST   | Ingest CSV data (text/csv) into the database. Body: raw CSV. Returns status string.                |
| `/years`                                                               | GET    | List all available tax years.                                                                      |
| `/filing-status`                                                       | GET    | Supported filing statuses (e.g., S, MFJ, MFS, HOH).                                                |
| `/rate?year={year}[&status={code}]`                                    | GET    | Tax brackets for a given year and optional filing status.                                          |
| `/breakdown`                                                           | POST   | Single-scenario breakdown. Body: JSON tax input `{ "year":2021, "status":"MFJ", "income":60000 }`. |
| `/summary?year={year}&status={code}`                                   | GET    | Total tax, average rate, bracket count, thresholds for specified year/status.                      |
| `/history?status={code}&metric={type}&startYear={YYYY}&endYear={YYYY}` | GET    | Year-over-year metric values (e.g., TOP\_RATE, AVERAGE\_RATE, COUNT).                              |
| `/simulate`                                                            | POST   | Bulk tax breakdowns. Body: JSON array of tax inputs. Returns list of responses.                    |

**Swagger UI**:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Actuator Endpoints

* Health:  `/actuator/health`
* Metrics: `/actuator/metrics`
* Info:    `/actuator/info`

---

## Project Structure

```
src/main/java/com/project/marginal/tax/calculator
├── bootstrap      # CSV import runner (TaxDataBootstrapper)
├── config         # Swagger, CORS, Jackson
├── controller     # REST API (TaxController)
├── dto            # Request/response DTOs (TaxInput, TaxRateDto, TaxPaidResponse, TaxSummaryResponse, YearMetric)
├── entity         # JPA entities (TaxRate, FilingStatus)
├── exception      # Global exception handlers
├── repository     # Spring Data JPA interfaces
├── service        # Business logic & import services
└── utility        # CSV parsing helpers
```

---

## Testing

* **Unit tests**: `mvn test` (requires Docker for Testcontainers)
* **Integration tests**: `mvn test` (requires Docker for Testcontainers)

---

## Continuous Integration

GitHub Actions (`.github/workflows/ci.yml`) runs:

* Build & test on Java 17

## Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/xyz`
3. Implement changes and add tests
4. Open a PR against `main`

---

## License

Apache License 2.0 — see [LICENSE](LICENSE)


