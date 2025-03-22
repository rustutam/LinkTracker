CREATE TABLE links
(
    id                 BIGSERIAL PRIMARY KEY,
    uri                TEXT NOT NULL UNIQUE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    created_at         TIMESTAMP DEFAULT now(),
);
