package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.domain.ids.TagId;

public interface SubscriptionTagsRepository {

    void save(SubscriptionId subscriptionId, TagId tagId);

    void delete(SubscriptionId subscriptionId, TagId tagId);
}
