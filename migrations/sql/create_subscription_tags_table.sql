CREATE TABLE subscription_tags
(
    subscription_id BIGINT NOT NULL,
    tag_id          BIGINT NOT NULL,
    created_at      TIMESTAMP DEFAULT now(),
    PRIMARY KEY (subscription_id, tag_id),
    FOREIGN KEY (subscription_id) REFERENCES subscriptions (id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);
