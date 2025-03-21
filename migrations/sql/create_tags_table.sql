CREATE TABLE tags
(
    id         BIGSERIAL PRIMARY KEY,
    tag    TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT now()
);
