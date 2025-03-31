package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.repository.database.SubscriptionFilterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcSubscriptionFilterRepository implements SubscriptionFilterRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(SubscriptionId subscriptionId, FilterId filterId) {
        jdbcTemplate.update(
            "INSERT INTO subscription_filters (subscription_id, filter_id) VALUES (?, ?)",
            subscriptionId.id(),
            filterId.id()
        );
    }

    @Override
    public void delete(SubscriptionId subscriptionId, FilterId filterId) {
        jdbcTemplate.update(
            "DELETE FROM subscription_filters WHERE subscription_id =? AND filter_id =?",
            subscriptionId.id(),
            filterId.id()
        );
    }
}
