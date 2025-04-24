package backend.academy.scrapper.repository.database.utilities.mapper;

import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.domain.ids.UserId;
import backend.academy.scrapper.models.dto.SubscriptionDto;

public class SubscriptionMapper {
    public static Subscription toDomain(SubscriptionDto entity) {
        return new Subscription(
            new SubscriptionId(entity.id()),
            new UserId(entity.userId()),
            new LinkId(entity.linkId())
        );

    }
}
