CREATE TABLE customer
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(20) NOT NULL,
    email          VARCHAR(50) NOT NULL UNIQUE ,
    password_hash  CHAR(60) NOT NULL,
    uuid           CHAR(36) UNIQUE
);