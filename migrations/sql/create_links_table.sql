CREATE TABLE links
(
    id                 BIGSERIAL PRIMARY KEY,
    uri                TEXT NOT NULL UNIQUE,
    tag_id             BIGINT,
    filter_id          BIGINT,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    created_at         TIMESTAMP DEFAULT now(),
    FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE SET NULL,
    FOREIGN KEY (filter_id) REFERENCES filters (id) ON DELETE SET NULL
);
