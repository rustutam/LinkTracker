CREATE TABLE IF NOT EXISTS scrapper.link_update
(
    id          BIGSERIAL PRIMARY KEY,
    link_id     BIGINT NOT NULL,
    uri         TEXT   NOT NULL,
    description TEXT,
    chat_ids    TEXT   NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT now()
);
