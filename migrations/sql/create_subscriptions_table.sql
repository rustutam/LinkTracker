CREATE TABLE subscriptions
(
    user_id       BIGINT NOT NULL,
    link_id       BIGINT NOT NULL,
    subscribed_at TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (user_id, link_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (link_id) REFERENCES links (id) ON DELETE CASCADE
);
