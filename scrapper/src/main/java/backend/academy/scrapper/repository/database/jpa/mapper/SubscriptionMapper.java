package backend.academy.scrapper.repository.database.jpa.mapper;

import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.entity.SubscriptionEntity;

public class SubscriptionMapper {
    public static Subscription toDomain(SubscriptionEntity entity) {
        return new Subscription(
            new SubscriptionId(entity.id()),
            UserMapper.toDomain(entity.user()),
            LinkMapper.map(entity.link()),
            TagMapper.map(entity.tags()),
            FilterMapper.map(entity.filters())
        );
    }

    public static SubscriptionEntity toEntity(Subscription domain) {
        return new SubscriptionEntity(
            domain.subscriptionId().id(),

            new SubscriptionId(entity.id()),
            UserMapper.toDomain(entity.user()),
            LinkMapper.map(entity.link()),
            TagMapper.map(entity.tags()),
            FilterMapper.map(entity.filters())
        );
    }
}
