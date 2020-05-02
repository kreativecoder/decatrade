CREATE TABLE users
(
    id         SERIAL PRIMARY KEY NOT NULL,
    username VARCHAR(10) unique,
    first_name character varying,
    last_name character varying,
    password character varying,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);
CREATE INDEX users_username_idx ON users (username);