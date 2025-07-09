CREATE SCHEMA security;

CREATE TABLE security.security_user(
    id SERIAL PRIMARY KEY,
    internal_user_id INTEGER NOT NULL UNIQUE,
    external_user_id VARCHAR(128) NOT NULL UNIQUE,
    email VARCHAR(128) NOT NULL UNIQUE,
    role VARCHAR(32) NOT NULL
);