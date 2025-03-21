CREATE TABLE filters
(
    id         BIGSERIAL PRIMARY KEY,
    filter    TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT now()
);

