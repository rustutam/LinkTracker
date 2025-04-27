package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);

    Subscription remove(Subscription subscription);

    Optional<Subscription> findById(SubscriptionId subscriptionId);

    Optional<Subscription> findByUserAndLink(User user, Link link);

    List<Subscription> findByUser(User user);

    List<Subscription> findByLink(Link link);
}
