CREATE TABLE subscriptions
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    link_id    BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT now(),

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (link_id) REFERENCES links (id) ON DELETE CASCADE
);
