CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    chat_id    BIGINT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT now()
);
