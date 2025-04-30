# Marginal Tax Rate Calculator

A Spring Boot REST API that provides historical United States income tax rates (from 1862 to 2021) and calculation endpoints to compute tax owed or breakdowns for a given year, filing status, and income.

---

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Configuration](#configuration)
    - [Running](#running)
- [Data Import](#data-import)
- [API Reference](#api-reference)
- [Configuration Packages](#configuration-packages)
- [Project Structure](#project-structure)
- [Error Handling](#error-handling)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- **List Years**: Retrieve all years for which tax data is available.
- **Filing Status Options**: Get supported filing statuses (Single, Married Filing Jointly, etc.).
- **Rate Lookup**: Fetch tax brackets and rates for a given year and optional filing status.
- **Tax Breakdown**: Calculate detailed tax owed per bracket for a specified income.
- **Summary**: Compute total tax, average rate, bracket count, thresholds, and legislative notes.
- **Historical Metrics** – NEW!  Compare _top rate, minimum rate, average rate,_ or _bracket count_ across any year range.
- **Bulk Simulation** – NEW!  POST an array of income scenarios and receive parallel breakdowns—perfect for what‑if tooling.
- **Swagger UI**: Interactive API documentation at `/swagger-ui/index.html`.
- **CORS Support**: Configurable cross-origin rules.

## Technology Stack

| Layer        | Choice                           |
|--------------|----------------------------------|
| Language     | **Java 17**                      |
| Framework    | **Spring Boot 3**                |
| Persistence  | Spring Data JPA + **PostgreSQL** |
| CSV  Parsing | OpenCSV                          |
| API  Docs    | springdoc‑openapi 3 / Swagger UI |
| Build        | Maven                            |


## Getting Started

### Prerequisites

- JDK 17+
- Maven 3.8+
- Any relational database (H2 in-memory, or configure PostgreSQL/MySQL)

### Installation

   ```bash
   git clone https://github.com/CHA0sTIG3R/Marginal-tax-rate-calculator.git
   cd Marginal-tax-rate-calculator
   ```

### Configuration

Minimal settings live in `src/main/resources/application.properties` (or `application.yml`).  Example for H2:
```properties
spring.datasource.url=jdbc:h2:mem:taxdb;DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update

# Import the CSV on first launch (requires profile `data-import`)
tax.import-on-startup=true
tax.data-url=https://raw.githubusercontent.com/CHA0sTIG3R/tax-data/main/Historical%20Income%20Tax%20Rates%20and%20Brackets%2C%201862-2021.csv
```


### Running

Import and boot the API in one step:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=data-import
```
Navigate to:
- **Swagger UI** – <http://localhost:8080/swagger-ui/index.html>
- **OpenAPI JSON** – <http://localhost:8080/v3/api-docs>

## Data Import
When the **`data-import`** profile is active _and_ the database is empty, **`TaxDataBootstrapper`** streams the canonical CSV from GitHub, converts rows via `CsvImportUtils`, and persists them through `TaxDataImportService`.

Navigate to:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- API Docs JSON: `http://localhost:8080/v3/api-docs`

## API Reference

Base path: `/api/v1/tax`

| Endpoint                                                        | Method | Description                                                                                             |
|-----------------------------------------------------------------|--------|---------------------------------------------------------------------------------------------------------|
| `/years`                                                        | GET    | List all tax years.                                                                                     |
| `/filing-status`                                                | GET    | Map of status codes → labels.                                                                           |
| `/rate?year=2021&status=MFJ`                                    | GET    | Brackets for a year (optionally status).                                                                |
| `/breakdown`                                                    | POST   | Bracket‑by‑bracket tax owed for one scenario. Body: `{ "year":2021, "status":"MFJ", "income":"60000" }` |
| `/summary?year=2021&status=MFJ`                                 | GET    | Summary statistics + note.                                                                              |
| `/notes?year=2021`                                              | GET    | Legislative note for the year.                                                                          |
| `/history?status=S&metric=TOP_RATE&startYear=1913&endYear=2021` | GET    | NEW!  Year‑over‑year metric values.                                                                     |
| `/simulate`                                                     | POST   | NEW!  Bulk tax breakdowns. Body: `[ {"year":2021,"status":"S","income":"45000"}, {...} ]`               |


## Project Structure

```
src/main/java/com/project/marginal/tax/calculator
├── bootstrap      # Data import runner
├── config         # Jackson, CORS, Swagger configs
├── controller     # REST layer
├── dto            # Request/response DTOs
├── entity         # JPA entities 
├── exception      # Centralised error handling 
├── repository     # Spring Data JPA interfaces
├── service        # Business logic & CSV ingest
└── utility        # Helpers (CSV parsing, number formatting)
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Write tests for new functionality
4. Submit a pull request

## License

Apache 2.0 — see [`LICENSE`](LICENSE) for full text.

