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

### Local with Docker (app + DB)

```bash
docker compose -f docker-compose.yml -f docker-compose.local.yml up -d --build
```

Logs: `docker compose logs -f` (local driver). App: http://localhost:8080/swagger-ui/index.html

---

## Configuration

- application.properties is env-first and contains no secrets.
- Copy `.env.example` → `.env` (for Compose) and set real values.

Key env vars:
- Datasource: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
- API key: `APP_INGEST_API_KEY` (protects `/api/v1/tax/upload`)
- Import: `TAX_IMPORT_ON_STARTUP`, `TAX_S3_BUCKET`, `TAX_S3_KEY`, `AWS_REGION` or `tax.s3-region`
- Schemas: `SPRING_FLYWAY_SCHEMAS`, `SPRING_FLYWAY_DEFAULT_SCHEMA`, `APP_DB_SCHEMA`

Local Docker override: see `docker-compose.local.yml` to build locally and run with a local Postgres (no CloudWatch logging).

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

GitHub Actions:
- `.github/workflows/ci.yml`: build & test on Java 17
- `.github/workflows/deploy.yml`: build image → push to ECR → deploy to EC2 via SSM
- Skip CI on a push: include `[skip ci]` in the commit message

## Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/xyz`
3. Implement changes and add tests
4. Open a PR against `main`

---

## License

Apache License 2.0 — see [LICENSE](LICENSE)


