package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.models.domain.ids.UserId;
import backend.academy.scrapper.models.dto.FilterDto;
import backend.academy.scrapper.models.dto.LinkDto;
import backend.academy.scrapper.models.dto.SubscriptionDto;
import backend.academy.scrapper.models.dto.TagDto;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import backend.academy.scrapper.repository.database.utilities.JdbcRowMapperUtil;
import backend.academy.scrapper.repository.database.utilities.mapper.FilterMapper;
import backend.academy.scrapper.repository.database.utilities.mapper.LinkMapper;
import backend.academy.scrapper.repository.database.utilities.mapper.SubscriptionMapper;
import backend.academy.scrapper.repository.database.utilities.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcSubscriptionRepository implements SubscriptionRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Subscription save(Subscription subscription) {

        SubscriptionId newSubscriptionId = new SubscriptionId(
            jdbcTemplate.queryForObject(
                "INSERT INTO scrapper.subscriptions (user_id, link_id) VALUES (?, ?) RETURNING id",
                Long.class,
                subscription.user().userId().id(),
                subscription.link().linkId().id()
            )
        );

        // Обновляем ID в доменной модели
        Subscription updatedSubscription = Subscription.builder()
            .subscriptionId(newSubscriptionId)
            .user(subscription.user())
            .link(subscription.link())
            .tags(subscription.tags())
            .filters(subscription.filters())
            .build();

        // Сохраняем связи с тегами
        insertTags(updatedSubscription);

        // Сохраняем связи с фильтрами
        insertFilters(updatedSubscription);


        return updatedSubscription;
    }

    private void insertTags(Subscription subscription) {
        for (Tag tag : subscription.tags()) {
            jdbcTemplate.update(
                """
                    INSERT INTO scrapper.subscription_tags (subscription_id, tag_id)
                    VALUES (?, ?)
                    """,
                subscription.subscriptionId().id(),
                tag.tagId().id()
            );
        }
    }

    private void insertFilters(Subscription subscription) {
        for (Filter filter : subscription.filters()) {
            jdbcTemplate.update(
                """
                    INSERT INTO scrapper.subscription_filters (subscription_id, filter_id)
                    VALUES (?, ?)
                    """,
                subscription.subscriptionId().id(),
                filter.filterId().id()
            );
        }
    }

    @Override
    public Subscription remove(Subscription subscription) {
        jdbcTemplate.update(
            "DELETE FROM scrapper.subscriptions WHERE user_id =? AND link_id =?",
            subscription.user().userId().id(),
            subscription.link().linkId().id()
        );

        return subscription;

    }

    @Override
    public Optional<Subscription> findById(SubscriptionId subscriptionId) {
        String sql = """
                SELECT
                    s.id                         AS subscription_id,
                    s.created_at                 AS subscription_created_at,

                    u.id                         AS user_id,
                    u.chat_id                    AS user_chat_id,
                    u.created_at                 AS user_created_at,

                    l.id                         AS link_id,
                    l.uri                        AS link_uri,
                    l.last_modified_date         AS link_last_modified_date,
                    l.created_at                 AS link_created_at

                FROM scrapper.subscriptions s
                JOIN scrapper.users u ON s.user_id = u.id
                JOIN scrapper.links l ON s.link_id = l.id

                WHERE s.id = (?)
            """;

        return jdbcTemplate.query(
            sql,
            rs -> {
                if (!rs.next()) {
                    return Optional.empty();
                }

                User user = new User(
                    new UserId(rs.getLong("user_id")),
                    new ChatId(rs.getLong("user_chat_id"))
                );

                Link link = new Link(
                    new LinkId(rs.getLong("link_id")),
                    URI.create(rs.getString("link_uri")),
                    rs.getObject("link_last_modified_date", OffsetDateTime.class)
                );

                List<Tag> tags = findTagsBySubscriptionId(subscriptionId).stream()
                    .map(TagMapper::toDomain)
                    .toList();

                List<Filter> filters = findFiltersBySubscriptionId(subscriptionId).stream()
                    .map(FilterMapper::toDomain)
                    .toList();

                Subscription subscription = Subscription.builder()
                    .subscriptionId(subscriptionId)
                    .user(user)
                    .link(link)
                    .tags(tags)
                    .filters(filters)
                    .build();

                return Optional.of(subscription);
            },
            subscriptionId.id());
    }

    @Override
    public Optional<Subscription> findByUserAndLink(User user, Link link) {
        String sql = """
                SELECT
                    s.id                         AS subscription_id,
                    s.created_at                 AS subscription_created_at,

                    u.id                         AS user_id,
                    u.chat_id                    AS user_chat_id,
                    u.created_at                 AS user_created_at,

                    l.id                         AS link_id,
                    l.uri                        AS link_uri,
                    l.last_modified_date         AS link_last_modified_date,
                    l.created_at                 AS link_created_at

                FROM scrapper.subscriptions s
                JOIN scrapper.users u ON s.user_id = u.id
                JOIN scrapper.links l ON s.link_id = l.id

                WHERE u.id = (?) AND l.id = (?)
            """;

        return jdbcTemplate.query(
            sql,
            rs -> {
                if (!rs.next()) {
                    return Optional.empty();
                }
                SubscriptionId subscriptionId = new SubscriptionId(
                    rs.getLong("subscription_id")
                );
                User findUser = new User(
                    new UserId(rs.getLong("user_id")),
                    new ChatId(rs.getLong("user_chat_id"))
                );

                Link findLink = new Link(
                    new LinkId(rs.getLong("link_id")),
                    URI.create(rs.getString("link_uri")),
                    rs.getObject("link_last_modified_date", OffsetDateTime.class)
                );

                List<Tag> tags = findTagsBySubscriptionId(subscriptionId).stream()
                    .map(TagMapper::toDomain)
                    .toList();

                List<Filter> filters = findFiltersBySubscriptionId(subscriptionId).stream()
                    .map(FilterMapper::toDomain)
                    .toList();

                Subscription subscription = Subscription.builder()
                    .subscriptionId(subscriptionId)
                    .user(findUser)
                    .link(findLink)
                    .tags(tags)
                    .filters(filters)
                    .build();

                return Optional.of(subscription);
            },
            user.userId().id(),
            link.linkId().id()
        );
    }

    @Override
    public List<Subscription> findByUser(User user) {
        String sql = """
                SELECT
                    s.id                         AS subscription_id,
                    s.created_at                 AS subscription_created_at,

                    u.id                         AS user_id,
                    u.chat_id                    AS user_chat_id,
                    u.created_at                 AS user_created_at,

                    l.id                         AS link_id,
                    l.uri                        AS link_uri,
                    l.last_modified_date         AS link_last_modified_date,
                    l.created_at                 AS link_created_at

                FROM scrapper.subscriptions s
                JOIN scrapper.users u ON s.user_id = u.id
                JOIN scrapper.links l ON s.link_id = l.id

                WHERE u.id = (?)
            """;

        return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> {
                SubscriptionId subscriptionId = new SubscriptionId(rs.getLong("subscription_id"));

                User findUser = new User(
                    new UserId(rs.getLong("user_id")),
                    new ChatId(rs.getLong("user_chat_id"))
                );

                Link findLink = new Link(
                    new LinkId(rs.getLong("link_id")),
                    URI.create(rs.getString("link_uri")),
                    rs.getObject("link_last_modified_date", OffsetDateTime.class)
                );

                List<Tag> tags = findTagsBySubscriptionId(subscriptionId).stream()
                    .map(TagMapper::toDomain)
                    .toList();

                List<Filter> filters = findFiltersBySubscriptionId(subscriptionId).stream()
                    .map(FilterMapper::toDomain)
                    .toList();

                return Subscription.builder()
                    .subscriptionId(subscriptionId)
                    .user(findUser)
                    .link(findLink)
                    .tags(tags)
                    .filters(filters)
                    .build();
            },
            user.userId().id()
        );
    }

    @Override
    public List<Subscription> findByLink(Link link) {
        String sql = """
            SELECT
                s.id                         AS subscription_id,
                s.created_at                 AS subscription_created_at,

                u.id                         AS user_id,
                u.chat_id                    AS user_chat_id,
                u.created_at                 AS user_created_at,

                l.id                         AS link_id,
                l.uri                        AS link_uri,
                l.last_modified_date         AS link_last_modified_date,
                l.created_at                 AS link_created_at

            FROM scrapper.subscriptions s
            JOIN scrapper.users u ON s.user_id = u.id
            JOIN scrapper.links l ON s.link_id = l.id

            WHERE l.id = ?
            """;

        return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> {
                SubscriptionId subscriptionId = new SubscriptionId(rs.getLong("subscription_id"));

                User findUser = new User(
                    new UserId(rs.getLong("user_id")),
                    new ChatId(rs.getLong("user_chat_id"))
                );

                Link findLink = new Link(
                    new LinkId(rs.getLong("link_id")),
                    URI.create(rs.getString("link_uri")),
                    rs.getObject("link_last_modified_date", OffsetDateTime.class)
                );

                List<Tag> tags = findTagsBySubscriptionId(subscriptionId).stream()
                    .map(TagMapper::toDomain)
                    .toList();

                List<Filter> filters = findFiltersBySubscriptionId(subscriptionId).stream()
                    .map(FilterMapper::toDomain)
                    .toList();

                return Subscription.builder()
                    .subscriptionId(subscriptionId)
                    .user(findUser)
                    .link(findLink)
                    .tags(tags)
                    .filters(filters)
                    .build();
            },
            link.linkId().id()
        );
    }

    private List<FilterDto> findFiltersBySubscriptionId(SubscriptionId subscriptionId) {
        String sql = """
            SELECT f.id, f.filter, f.created_at
            FROM scrapper.subscription_tags st
            LEFT JOIN scrapper.filters f ON st.tag_id = f.id
            WHERE st.subscription_id = (?)
            """;
        List<FilterDto> filterEntities = jdbcTemplate.query(
            sql,
            JdbcRowMapperUtil::mapRowToFilter,
            subscriptionId.id()
        );

        return filterEntities.stream().toList();

    }

    private List<TagDto> findTagsBySubscriptionId(SubscriptionId subscriptionId) {
        String sql = """
            SELECT t.id, t.tag, t.created_at
            FROM scrapper.subscription_tags st
            LEFT JOIN scrapper.tags t ON st.tag_id = t.id
            WHERE st.subscription_id = (?)
            """;
        List<TagDto> tagEntities = jdbcTemplate.query(
            sql,
            JdbcRowMapperUtil::mapRowToTag,
            subscriptionId.id()
        );

        return tagEntities.stream().toList();
    }

}
