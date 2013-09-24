CREATE SCHEMA test AUTHORIZATION rc;
ALTER ROLE rc SET search_path = test;
SET search_path = test;