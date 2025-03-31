package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;

public interface SubscriptionFilterRepository {

    void save(SubscriptionId subscriptionId, FilterId filterId);

    void delete(SubscriptionId subscriptionId, FilterId filterId);
}
