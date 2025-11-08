-- Enforce idempotency at the database layer
-- Prevent duplicate brackets for the same (year, status, start, end)
CREATE UNIQUE INDEX IF NOT EXISTS uq_hist_rates_year_status_start_end
  ON historical_tax_rates (year, status, range_start, range_end);

