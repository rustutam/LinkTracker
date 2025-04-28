INSERT INTO scrapper.users (id, chat_id)
VALUES (1, 100),
       (2, 101),
       (3, 102),
       (4, 103),
       (5, 104);

INSERT INTO scrapper.links (id, uri, last_modified_date)
VALUES (1, 'https://github.com/java-rustutam/semester1', '2024-01-02T10:00:00Z'),
       (2, 'https://github.com/java-rustutam/semester2', '2024-01-03T10:00:00Z'),
       (3, 'https://github.com/java-rustutam/semester3', '2024-01-04T10:00:00Z'),
       (4, 'https://github.com/java-rustutam/semester4', '2024-01-05T10:00:00Z'),
       (5, 'https://github.com/java-rustutam/semester5', '2024-01-06T10:00:00Z'),
       (6, 'https://github.com/java-rustutam/semester6', '2024-01-07T10:00:00Z');

INSERT INTO scrapper.subscriptions (user_id, link_id, created_at)
VALUES (1, 1, '2024-01-02T10:00:00Z'),
       (1, 2, '2024-01-03T10:00:00Z'),
       (1, 3, '2024-01-12T10:00:00Z'),
       (2, 4, '2024-01-23T10:00:00Z'),
       (2, 5, '2024-01-24T10:00:00Z'),
       (4, 1, '2024-01-12T10:00:00Z'),
       (5, 1, '2024-01-01T10:00:00Z');

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

