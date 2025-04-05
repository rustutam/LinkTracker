package backend.academy.scrapper.repository.database.utilities.mapper;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.domain.ids.UserId;
import backend.academy.scrapper.models.entities.LinkEntity;
import backend.academy.scrapper.models.entities.SubscriptionEntity;
import java.net.URI;

public class SubscriptionMapper {
    public static Subscription toDomain(SubscriptionEntity entity) {
        return new Subscription(
            new UserId(entity.userId()),
            new LinkId(entity.linkId())
        );

    }
}
