package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import java.util.List;
import java.util.Optional;

public interface FilterRepository {
    Optional<Filter> findById(FilterId filterId);

    Optional<Filter> findByFilter(String filter);

    List<Filter> findBySubscriptionId(SubscriptionId subscriptionId);

    Filter save(String filter);

    void deleteById(FilterId filterId);
}
