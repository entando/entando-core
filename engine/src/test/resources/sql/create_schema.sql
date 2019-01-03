-- Added to avoid Embedded Derby / Spring compatibility issue where
-- Spring dataSource fails to use schema if it wasn't created before
CREATE SCHEMA SA;