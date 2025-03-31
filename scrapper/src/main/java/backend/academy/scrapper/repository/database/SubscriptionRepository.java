package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    Subscription save(User user, Link link);

    Subscription remove(User user, Link link);

    List<Link> findAllLinksByChatId(ChatId chatId);

    List<ChatId> findAllChatIdsByLinkId(LinkId linkId);

    Optional<Subscription> findById(SubscriptionId subscriptionId);

    // Методы для управления связями many-to-many:
    void addFilterToSubscription(Long subscriptionId, Long filterId);
    void removeFilterFromSubscription(Long subscriptionId, Long filterId);
    List<Filter> findFiltersBySubscriptionId(Long subscriptionId);

    void addTagToSubscription(Long subscriptionId, Long tagId);
    void removeTagFromSubscription(Long subscriptionId, Long tagId);
    List<Tag> findTagsBySubscriptionId(Long subscriptionId);
}
