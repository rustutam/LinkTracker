CREATE SCHEMA IF NOT EXISTS scrapper;

-- Пользователи
CREATE TABLE IF NOT EXISTS scrapper.users
(
    id         BIGSERIAL PRIMARY KEY,
    chat_id    BIGINT NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Ссылки
CREATE TABLE IF NOT EXISTS scrapper.links
(
    id                 BIGSERIAL PRIMARY KEY,
    uri                TEXT NOT NULL UNIQUE,
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT now(),
    created_at         TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Подписки. Таблица для связывания пользователей и ссылок
CREATE TABLE IF NOT EXISTS scrapper.subscriptions
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    link_id    BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),

    FOREIGN KEY (user_id) REFERENCES scrapper.users (id) ON DELETE CASCADE,
    FOREIGN KEY (link_id) REFERENCES scrapper.links (id) ON DELETE CASCADE,

    CONSTRAINT unique_user_link UNIQUE (user_id, link_id)
);

-- Теги
CREATE TABLE IF NOT EXISTS scrapper.tags
(
    id         BIGSERIAL PRIMARY KEY,
    tag        TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Фильтры
CREATE TABLE IF NOT EXISTS scrapper.filters
(
    id         BIGSERIAL PRIMARY KEY,
    filter     TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

--Таблица связи подписок и тегов
CREATE TABLE IF NOT EXISTS scrapper.subscription_tags
(
    subscription_id BIGINT NOT NULL,
    tag_id          BIGINT NOT NULL,
    PRIMARY KEY (subscription_id, tag_id),
    FOREIGN KEY (subscription_id) REFERENCES scrapper.subscriptions (id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES scrapper.tags (id) ON DELETE CASCADE
);

--Таблица связи подписок и фильтров
CREATE TABLE IF NOT EXISTS scrapper.subscription_filters
(
    subscription_id BIGINT NOT NULL,
    filter_id       BIGINT NOT NULL,
    PRIMARY KEY (subscription_id, filter_id),
    FOREIGN KEY (subscription_id) REFERENCES scrapper.subscriptions (id) ON DELETE CASCADE,
    FOREIGN KEY (filter_id) REFERENCES scrapper.filters (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_subscriptions_user_id
    ON scrapper.subscriptions (user_id);

CREATE INDEX IF NOT EXISTS idx_subscriptions_link_id
    ON scrapper.subscriptions (link_id);

CREATE INDEX IF NOT EXISTS idx_subscription_tags_tag_id
    ON scrapper.subscription_tags (tag_id);

CREATE INDEX IF NOT EXISTS idx_subscription_filters_filter_id
    ON scrapper.subscription_filters (filter_id);

CREATE INDEX IF NOT EXISTS idx_links_last_modified_date
    ON scrapper.links (last_modified_date);

CREATE INDEX IF NOT EXISTS idx_links_created_at
    ON scrapper.links (created_at);

CREATE INDEX IF NOT EXISTS idx_tags_tag_exact
    ON scrapper.tags (tag);

CREATE INDEX IF NOT EXISTS idx_filters_filter_exact
    ON scrapper.filters (filter);
