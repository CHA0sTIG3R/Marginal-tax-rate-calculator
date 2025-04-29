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
    - [Running the Application](#running-the-application)
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
- **Swagger UI**: Interactive API documentation at `/swagger-ui/index.html`.
- **CORS Support**: Configurable cross-origin rules.

## Technology Stack

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- Jackson (for JSON serialization)
- springdoc-openapi (Swagger / OpenAPI 3)
- OpenCSV (CSV parsing)
- Any JDBC-compatible database or H2 (default in-memory)
- Maven build tool

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.8+
- A relational database (H2, PostgreSQL (current choice), MySQL) or use the default H2 in-memory

### Installation

**Clone the repository**:

   ```bash
   git clone https://github.com/CHA0sTIG3R/Marginal-tax-rate-calculator.git
   cd Marginal-tax-rate-calculator
   ```

### Configuration

Edit `src/main/resources/application.properties` (or `application.yml`) to configure your database and import behavior:

```properties
# DataSource settings (H2 example)
spring.datasource.url=jdbc:h2:mem:taxdb;DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=

# JPA settings
spring.jpa.hibernate.ddl-auto=update

# Import on startup (profile 'data-import' must be active)
tax.import-on-startup=true
# Public URL where the CSV lives
tax.data-url=https://raw.githubusercontent.com/CHA0sTIG3R/tax-data/refs/heads/main/Historical%20Income%20Tax%20Rates%20and%20Brackets%2C%201862-2021.csv
```

Activate the data-import profile when you run:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=data-import
```

### Running the Application

Build and run:

```bash
mvn clean package
java -jar target/marginal-tax-calculator-0.0.1-SNAPSHOT.jar --spring.profiles.active=data-import
```

Navigate to:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- API Docs JSON: `http://localhost:8080/v3/api-docs`

## Data Import

On first startup (when the database is empty and `tax.import-on-startup=true`), the `TaxDataBootstrapper` loads bracket entries via `TaxDataImportService` and `CsvImportUtils`.

## API Reference

Base path: `/api/v1/tax`

| Endpoint                          | Method | Description                                                                                               |
|-----------------------------------|--------|-----------------------------------------------------------------------------------------------------------|
| `/years`                          | GET    | List all available tax years.                                                                             |
| `/filing-status`                  | GET    | Get map of filing status codes to labels.                                                                 |
| `/rate?year=<>&status=<optional>` | GET    | Retrieve tax brackets for a year and optional status.                                                     |
| `/breakdown`                      | POST   | Calculate bracket-by-bracket tax breakdown. Request body: `{"year":2021,"status":"MFJ","income":"60000"}` |
| `/notes?year=<>`                  | GET    | Retrieve legislative note for a given year.                                                               |
| `/summary?year=<>&status=<>`      | GET    | Get summary: bracket count, thresholds, average rate, and note.                                           |

## Configuration Packages

All custom configuration lives under `com.project.marginal.tax.calculator.config`:

- `JacksonConfig`: Customizes the `ObjectMapper` (ISO dates, non-null inclusion, etc.).
- `CorsConfig`: Defines CORS mappings, allowed origins/methods, and credential rules.
- `SwaggerConfig`: Supplies an `OpenAPI` bean for title, version, and description of the API.

## Project Structure

```
src/main/java/com/project/marginal/tax/calculator
├── bootstrap      # Data import runner
├── config         # Jackson, CORS, Swagger configs
├── controller     # REST controllers (TaxController)
├── dto            # Request/response DTOs
├── entity         # JPA entities (TaxRate, FilingStatus)
├── exception      # GlobalExceptionHandler, ErrorResponse
├── repository     # Spring Data JPA repositories
├── service        # Business logic (TaxService, TaxDataImportService)
└── utility        # CSV import utils, number formatting, etc.
```

## Error Handling

`GlobalExceptionHandler` (annotated with `@RestControllerAdvice`) converts exceptions like validation failures, type mismatches, and generic errors into clean HTTP responses using `ErrorResponse`.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Write tests for new functionality
4. Submit a pull request

## License

This project is licensed under the Apache License — see [LICENSE](LICENSE) for details.

