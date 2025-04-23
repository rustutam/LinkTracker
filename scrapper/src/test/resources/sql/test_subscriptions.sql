INSERT INTO scrapper.users (id, chat_id)
VALUES (1, 100),
       (2, 101),
       (3, 102),
       (4, 103),
       (5, 104);

INSERT INTO scrapper.links (id, uri, last_modified_date)
VALUES (1, 'https://github.com/java-rustutam/semester1', '2024-01-01 10:00:00'),
       (2, 'https://github.com/java-rustutam/semester2', '2024-01-02 10:00:00'),
       (3, 'https://github.com/java-rustutam/semester3', '2024-01-03 10:00:00'),
       (4, 'https://github.com/java-rustutam/semester4', '2024-01-04 10:00:00'),
       (5, 'https://github.com/java-rustutam/semester5', '2024-01-05 10:00:00'),
       (6, 'https://github.com/java-rustutam/semester6', '2024-01-06 10:00:00');

INSERT INTO scrapper.subscriptions (user_id, link_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (2, 5),
       ( 4, 2),
       ( 5, 2);

INSERT INTO scrapper.filters (id, filter)
VALUES (1, 'filter1'),
       (2, 'filter2'),
       (3, 'filter3'),
       (4, 'filter4'),
       (5, 'filter5');

INSERT INTO scrapper.subscription_filters (subscription_id, filter_id)
VALUES (1, 1),
       (1, 3),
       (2, 2);

INSERT INTO scrapper.tags (id, tag)
VALUES (1, 'tag1'),
       (2, 'tag2'),
       (3, 'tag3'),
       (4, 'tag4'),
       (5, 'tag5');

INSERT INTO scrapper.subscription_tags (subscription_id, tag_id)
VALUES (1, 1),
       (1, 3),
       (2, 2);

