-- Historical tax rates table
CREATE TABLE IF NOT EXISTS historical_tax_rates (
  id           BIGSERIAL PRIMARY KEY,
  year         INTEGER      NOT NULL,
  status       VARCHAR(8)   NOT NULL,
  rate         REAL         NOT NULL,
  range_start  NUMERIC(19,2) NOT NULL,
  range_end    NUMERIC(19,2)
);

-- Helpful composite index for common lookups
CREATE INDEX IF NOT EXISTS idx_tax_rates_year_status
  ON historical_tax_rates (year, status);

-- Years with no income tax (historical note)
CREATE TABLE IF NOT EXISTS no_income_tax_year (
  year     INTEGER PRIMARY KEY,
  message  TEXT     NOT NULL
);

