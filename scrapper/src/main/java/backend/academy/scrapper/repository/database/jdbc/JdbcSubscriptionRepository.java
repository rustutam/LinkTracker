package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import backend.academy.scrapper.repository.database.jdbc.mapper.SubscriptionResultSetExtractor;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcSubscriptionRepository implements SubscriptionRepository {
    private static final String BASE_SELECT = """
        SELECT
            s.id                         AS subscription_id,
            s.created_at                 AS subscription_created_at,

            u.id                         AS user_id,
            u.chat_id                    AS user_chat_id,
            u.created_at                 AS user_created_at,

            l.id                         AS link_id,
            l.uri                        AS link_uri,
            l.last_modified_date         AS link_last_modified_date,
            l.created_at                 AS link_created_at,

            t.id                         AS tag_id,
            t.tag                        AS tag_value,

            f.id                         AS filter_id,
            f.filter                     AS filter_value
        FROM scrapper.subscriptions s
        JOIN scrapper.users u  ON s.user_id = u.id
        JOIN scrapper.links l  ON s.link_id = l.id
        LEFT JOIN scrapper.subscription_tags st ON s.id = st.subscription_id
        LEFT JOIN scrapper.tags t               ON st.tag_id = t.id
        LEFT JOIN scrapper.subscription_filters sf ON s.id = sf.subscription_id
        LEFT JOIN scrapper.filters f               ON sf.filter_id = f.id
        """;
    public static final String SELECT_BY_LINK = BASE_SELECT + " WHERE l.id = ?";
    public static final String SELECT_BY_USER = BASE_SELECT + " WHERE u.id = ?";
    public static final String SELECT_BY_USER_AND_LINK = BASE_SELECT + " WHERE u.id = ? AND l.id = ?";
    public static final String SELECT_BY_ID = BASE_SELECT + " WHERE s.id = ?";
    public static final String DELETE_SQL = "DELETE FROM scrapper.subscriptions WHERE user_id = ? AND link_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final SubscriptionResultSetExtractor extractor;


    @Override
    @Transactional
    public Subscription save(Subscription subscription) {
        SubscriptionId newSubscriptionId = new SubscriptionId(
            jdbcTemplate.queryForObject(
                "INSERT INTO scrapper.subscriptions (user_id, link_id) VALUES (?, ?) RETURNING id",
                Long.class,
                subscription.user().userId().id(),
                subscription.link().linkId().id()
            )
        );

        batchInsert("subscription_tags", newSubscriptionId.id(),
            subscription.tags().stream().map(t -> t.tagId().id()).toList());
        batchInsert("subscription_filters", newSubscriptionId.id(),
            subscription.filters().stream().map(f -> f.filterId().id()).toList());

        // Обновляем ID в доменной модели
        return Subscription.builder()
            .subscriptionId(newSubscriptionId)
            .user(subscription.user())
            .link(subscription.link())
            .tags(subscription.tags())
            .filters(subscription.filters())
            .build();
    }

    private void batchInsert(String table, Long subscriptionId, List<Long> ids) {
        if (ids.isEmpty()) return;
        String sql = String.format(
            "INSERT INTO scrapper.%s (subscription_id, %s_id) VALUES (?, ?)",
            table, table.replace("subscription_", "")
        );
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, subscriptionId);
                ps.setLong(2, ids.get(i));
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });
    }

    @Override
    @Transactional
    public Subscription remove(Subscription subscription) {
        jdbcTemplate.update(
            DELETE_SQL,
            subscription.user().userId().id(),
            subscription.link().linkId().id()
        );
        return subscription;
    }

    @Override
    public Optional<Subscription> findById(SubscriptionId id) {
        List<Subscription> list = jdbcTemplate.query(SELECT_BY_ID, extractor, id.id());
        return Optional.ofNullable(DataAccessUtils.singleResult(list));
    }

    @Override
    public Optional<Subscription> findByUserAndLink(User user, Link link) {
        List<Subscription> list = jdbcTemplate.query(
            SELECT_BY_USER_AND_LINK,
            extractor,
            user.userId().id(),
            link.linkId().id()
        );
        return Optional.ofNullable(DataAccessUtils.singleResult(list));
    }

    @Override
    public List<Subscription> findByUser(User user) {
        return jdbcTemplate.query(SELECT_BY_USER, extractor, user.userId().id());
    }

    @Override
    public List<Subscription> findByLink(Link link) {
        return jdbcTemplate.query(SELECT_BY_LINK, extractor, link.linkId().id());
    }

}
