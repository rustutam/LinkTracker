CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    chat_id    BIGINT uri,
    created_at TIMESTAMP DEFAULT now()
);
