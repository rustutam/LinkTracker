CREATE TABLE subscription_filters
(
    subscription_id BIGINT NOT NULL,
    filter_id       BIGINT NOT NULL,
    created_at      TIMESTAMP DEFAULT now(),
    PRIMARY KEY (subscription_id, filter_id),
    FOREIGN KEY (subscription_id) REFERENCES subscriptions (id) ON DELETE CASCADE,
    FOREIGN KEY (filter_id) REFERENCES filters (id) ON DELETE CASCADE
);
