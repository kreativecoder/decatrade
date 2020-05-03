CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY NOT NULL,
    username   VARCHAR(10) unique,
    first_name character varying,
    last_name  character varying,
    password   character varying,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);
CREATE INDEX IF NOT EXISTS users_username_idx ON users (username);

CREATE TABLE IF NOT EXISTS transactions
(
    id                 SERIAL PRIMARY KEY NOT NULL,
    quantity           BIGINT             NOT NULL,
    reference          VARCHAR(50) UNIQUE NOT NULL,
    amount             NUMERIC            NOT NULL,
    transaction_type   VARCHAR(4)         NOT NULL,
    transaction_status VARCHAR(10)        NOT NULL,
    symbol             VARCHAR(10)        NOT NULL,
    user_id            BIGINT             NOT NULL,
    created_at         TIMESTAMP WITH TIME ZONE,
    updated_at         TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_txns_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS txns_reference_idx ON transactions (reference);
CREATE INDEX IF NOT EXISTS txns_user_id_idx ON transactions (user_id);

CREATE TABLE IF NOT EXISTS stocks
(
    id         SERIAL PRIMARY KEY NOT NULL,
    quantity   BIGINT             NOT NULL,
    symbol     VARCHAR(10) UNIQUE NOT NULL,
    user_id    BIGINT             NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_stocks_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS stocks_user_id_idx ON stocks (user_id);