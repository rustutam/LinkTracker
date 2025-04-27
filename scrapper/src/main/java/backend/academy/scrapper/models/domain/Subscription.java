package backend.academy.scrapper.models.domain;

import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    private SubscriptionId subscriptionId;
    private User user;
    private Link link;
    private List<Tag> tags;
    private List<Filter> filters;
    private OffsetDateTime createdAt;
}
