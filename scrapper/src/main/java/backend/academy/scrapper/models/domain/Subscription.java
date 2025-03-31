package backend.academy.scrapper.models.domain;

import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class Subscription {
    private SubscriptionId subscriptionId;
    private User user;
    private Link link;

    public Subscription(User user, Link link) {
        this.subscriptionId = new SubscriptionId(0L);
        this.user = user;
        this.link = link;
    }

}
