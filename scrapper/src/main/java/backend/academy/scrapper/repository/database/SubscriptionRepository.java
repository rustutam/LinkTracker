package backend.academy.scrapper.repository.database;

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
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    SubscriptionId save(UserId userId, LinkId linkId);

    Subscription remove(User user, Link link);

    List<Link> findAllLinksByChatId(ChatId chatId);

    List<ChatId> findAllChatIdsByLinkId(LinkId linkId);

    Optional<Subscription> findById(SubscriptionId subscriptionId);

    Optional<Subscription> findByLinkIdAndUserId(LinkId linkId, UserId userId);

    // Методы для управления связями many-to-many:
    void addFilterToSubscription(SubscriptionId subscriptionId, FilterId filterId);
    void removeFilterFromSubscription(SubscriptionId subscriptionId, FilterId filterId);
    List<Filter> findFiltersBySubscriptionId(SubscriptionId subscriptionId);

    void addTagToSubscription(SubscriptionId subscriptionId, TagId tagId);
    void removeTagFromSubscription(SubscriptionId subscriptionId, TagId tagId);
    List<Tag> findTagsBySubscriptionId(SubscriptionId subscriptionId);
}
