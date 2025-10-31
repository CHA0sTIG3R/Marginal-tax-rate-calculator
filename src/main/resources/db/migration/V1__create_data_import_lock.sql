CREATE TABLE IF NOT EXISTS data_import_lock (
  id INT PRIMARY KEY,
  completed_at TIMESTAMPTZ
);

