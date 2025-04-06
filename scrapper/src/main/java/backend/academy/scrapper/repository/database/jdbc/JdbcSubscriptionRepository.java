package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.exceptions.NotExistTgChatException;
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
import backend.academy.scrapper.models.entities.FilterEntity;
import backend.academy.scrapper.models.entities.SubscriptionEntity;
import backend.academy.scrapper.models.entities.TagEntity;
import backend.academy.scrapper.repository.database.LinkRepository;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import backend.academy.scrapper.repository.database.ChatRepository;
import backend.academy.scrapper.repository.database.utilities.JdbcRowMapperUtil;
import backend.academy.scrapper.repository.database.utilities.mapper.FilterMapper;
import backend.academy.scrapper.repository.database.utilities.mapper.SubscriptionMapper;
import backend.academy.scrapper.repository.database.utilities.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcSubscriptionRepository implements SubscriptionRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public SubscriptionId save(UserId userId, LinkId linkId) {
        SubscriptionId subscriptionId = new SubscriptionId(
            jdbcTemplate.queryForObject(
                "INSERT INTO subscriptions (user_id, link_id) VALUES (?, ?) RETURNING id",
                Long.class,
                userId.id(),
                linkId.id()
            )
        );
        return subscriptionId;
    }

    @Override
    public Subscription remove(User user, Link link) {
        SubscriptionId subscriptionId = new SubscriptionId(
            jdbcTemplate.queryForObject(
                "DELETE FROM subscriptions WHERE user_id =? AND link_id =? RETURNING id",
                Long.class,
                user.userId().id(),
                link.linkId().id()
            )
        );

        return Subscription.builder()
            .subscriptionId(subscriptionId)
            .userId(user.userId())
            .linkId(link.linkId())
            .build();

    }

    @Override
    public List<Link> findAllLinksByChatId(ChatId chatId) {
        return List.of();
    }

    @Override
    public List<ChatId> findAllChatIdsByLinkId(LinkId linkId) {
        String sql = """
        SELECT u.chat_id
        FROM subscriptions s
        JOIN users u ON s.user_id = u.id
        WHERE s.link_id = (?)
        """;
        return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new ChatId(rs.getLong("chat_id")),
            linkId.id()
        );
    }

    @Override
    public Optional<Subscription> findById(SubscriptionId subscriptionId) {
        var userEntities = jdbcTemplate.query(
            "SELECT * FROM subscriptions WHERE chat_id = (?)",
            JdbcRowMapperUtil::mapRowToSubscriptionEntity,
            subscriptionId.id()
        );

        return userEntities.stream().map(SubscriptionMapper::toDomain).findFirst();
    }

    @Override
    public Optional<Subscription> findByLinkIdAndUserId(LinkId linkId, UserId userId) {
        String sql = """
            SELECT *
            FROM subscriptions s
            WHERE s.link_id = (?) AND s.user_id = (?)
            """;
        List<SubscriptionEntity> subscriptions = jdbcTemplate.query(
            sql,
            JdbcRowMapperUtil::mapRowToSubscriptionEntity,
            linkId.id(),
            userId.id()
        );

        return subscriptions.stream().map(SubscriptionMapper::toDomain).findFirst();
    }


    @Override
    public void addFilterToSubscription(SubscriptionId subscriptionId, FilterId filterId) {
        jdbcTemplate.update(
            "INSERT INTO subscription_filters (subscription_id, filter_id) VALUES (?, ?)",
            subscriptionId.id(),
            filterId.id()
        );
    }

    @Override
    public void removeFilterFromSubscription(SubscriptionId subscriptionId, FilterId filterId) {
        jdbcTemplate.update(
            "DELETE FROM subscription_filters WHERE subscription_id =? AND filter_id =?",
            subscriptionId.id(),
            filterId.id()
        );
    }


    @Override
    public List<Filter> findFiltersBySubscriptionId(SubscriptionId subscriptionId) {
        String sql = """
            SELECT f.id, f.filter, f.created_at
            FROM subscription_tags st
            LEFT JOIN filters f ON st.tag_id = f.id
            WHERE st.subscription_id = (?)
            """;
        List<FilterEntity> filterEntities = jdbcTemplate.query(
            sql,
            JdbcRowMapperUtil::mapRowToFilter,
            subscriptionId.id()
        );

        return filterEntities.stream().map(FilterMapper::toDomain).toList();

    }

    @Override
    public void addTagToSubscription(SubscriptionId subscriptionId, TagId tagId) {
        jdbcTemplate.update(
            "INSERT INTO subscription_tags (subscription_id, tag_id) VALUES (?, ?)",
            subscriptionId.id(),
            tagId.id()
        );
    }

    @Override
    public void removeTagFromSubscription(SubscriptionId subscriptionId, TagId tagId) {
        jdbcTemplate.update(
            "DELETE FROM subscription_tags WHERE subscription_id =? AND tag_id =?",
            subscriptionId.id(),
            tagId.id()
        );
    }

    @Override
    public List<Tag> findTagsBySubscriptionId(SubscriptionId subscriptionId) {
        String sql = """
            SELECT t.id, t.tag, t.created_at
            FROM subscription_tags st
            LEFT JOIN tags t ON st.tag_id = t.id
            WHERE st.subscription_id = (?)
            """;
        List<TagEntity> tagEntities = jdbcTemplate.query(
            sql,
            JdbcRowMapperUtil::mapRowToTag,
            subscriptionId.id()
        );

        return tagEntities.stream().map(TagMapper::toDomain).toList();
    }

}
