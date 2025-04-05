package backend.academy.scrapper.models.domain;

import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.domain.ids.UserId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class Subscription {
    private SubscriptionId subscriptionId;
    private UserId userId;
    private LinkId linkId;

    public Subscription(UserId userId, LinkId linkId) {
        this.subscriptionId = new SubscriptionId(0L);
        this.userId = userId;
        this.linkId = linkId;
    }

}
