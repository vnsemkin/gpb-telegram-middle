-- Create the account table
CREATE TABLE account (
                         id             SERIAL PRIMARY KEY,
                         uuid           CHAR(36) UNIQUE,
                         account_name   VARCHAR(50) NOT NULL,
                         balance        NUMERIC(15, 2) NOT NULL
);

-- Create the customer table
CREATE TABLE customer (
                          id             SERIAL PRIMARY KEY,
                          tg_id          BIGINT NOT NULL UNIQUE,
                          first_name     VARCHAR(20) NOT NULL,
                          username       VARCHAR(20) NOT NULL,
                          email          VARCHAR(50) NOT NULL UNIQUE,
                          password_hash  CHAR(60) NOT NULL,
                          uuid           CHAR(36) UNIQUE,
                          account_id     BIGINT UNIQUE,
                          CONSTRAINT fk_account
                              FOREIGN KEY (account_id)
                                  REFERENCES account(id),
                          CONSTRAINT uq_account_id UNIQUE (account_id)
);