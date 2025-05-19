TRUNCATE TABLE scrapper.users CASCADE;
TRUNCATE TABLE scrapper.links CASCADE;
TRUNCATE TABLE scrapper.tags CASCADE;
TRUNCATE TABLE scrapper.filters CASCADE;
TRUNCATE TABLE scrapper.subscriptions CASCADE;
TRUNCATE TABLE scrapper.subscription_tags CASCADE;
TRUNCATE TABLE scrapper.subscription_filters CASCADE;
TRUNCATE TABLE scrapper.link_update CASCADE;

ALTER SEQUENCE scrapper.users_id_seq RESTART WITH 1;
ALTER SEQUENCE scrapper.links_id_seq RESTART WITH 1;
ALTER SEQUENCE scrapper.tags_id_seq RESTART WITH 1;
ALTER SEQUENCE scrapper.filters_id_seq RESTART WITH 1;
ALTER SEQUENCE scrapper.subscriptions_id_seq RESTART WITH 1;
ALTER SEQUENCE scrapper.link_update_id_seq RESTART WITH 1;
