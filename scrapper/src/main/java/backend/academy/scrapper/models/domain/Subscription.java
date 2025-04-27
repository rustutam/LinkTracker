package backend.academy.scrapper.models.domain;

import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.domain.ids.UserId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    private SubscriptionId subscriptionId;
    private User user;
    private Link link;
    private List<Tag> tags;
    private List<Filter> filters;

    public Subscription(User user, Link link, List<Tag> tags, List<Filter> filters) {
        this.subscriptionId = new SubscriptionId(0L);
        this.user = user;
        this.link = link;
        this.tags = List.of();
        this.filters = List.of();
    }

}
