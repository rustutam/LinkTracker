CREATE SCHEMA IF NOT EXISTS scrapper;

-- Пользователи
CREATE TABLE scrapper.users
(
    id         BIGSERIAL PRIMARY KEY,
    chat_id    BIGINT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT now()
);

-- Ссылки
CREATE TABLE scrapper.links
(
    id                 BIGSERIAL PRIMARY KEY,
    uri                TEXT NOT NULL UNIQUE,
    last_modified_date TIMESTAMP DEFAULT now(),
    created_at         TIMESTAMP DEFAULT now(),
);

-- Подписки. Таблица для связывания пользователей и ссылок
CREATE TABLE scrapper.subscriptions
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    link_id    BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT now(),

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (link_id) REFERENCES links (id) ON DELETE CASCADE,

    CONSTRAINT unique_user_link UNIQUE (user_id, link_id)
);

-- Теги
CREATE TABLE scrapper.tags
(
    id         BIGSERIAL PRIMARY KEY,
    tag        TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT now()
);

-- Фильтры
CREATE TABLE scrapper.filters
(
    id         BIGSERIAL PRIMARY KEY,
    filter    TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT now()
);

--Таблица связи подписок и тегов
CREATE TABLE scrapper.subscription_tags
(
    subscription_id BIGINT NOT NULL,
    tag_id          BIGINT NOT NULL,
    created_at      TIMESTAMP DEFAULT now(),
    PRIMARY KEY (subscription_id, tag_id),
    FOREIGN KEY (subscription_id) REFERENCES subscriptions (id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);

--Таблица связи подписок и фильтров
CREATE TABLE scrapper.subscription_filters
(
    subscription_id BIGINT NOT NULL,
    filter_id       BIGINT NOT NULL,
    created_at      TIMESTAMP DEFAULT now(),
    PRIMARY KEY (subscription_id, filter_id),
    FOREIGN KEY (subscription_id) REFERENCES subscriptions (id) ON DELETE CASCADE,
    FOREIGN KEY (filter_id) REFERENCES filters (id) ON DELETE CASCADE
);

-- TODO добавить индексы на поля
