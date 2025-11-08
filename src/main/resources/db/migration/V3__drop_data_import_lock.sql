-- Drop obsolete data import lock table
-- Handles both default schema search_path and explicit flyway_schema
DROP TABLE IF EXISTS data_import_lock;
DROP TABLE IF EXISTS flyway_schema.data_import_lock;

