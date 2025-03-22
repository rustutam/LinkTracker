CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    chat_id    BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT now()
);
