package backend.academy.scrapper.repository.database.jpa.mapper;

import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.entity.SubscriptionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionMapper {
    private final UserMapper userMapper;
    private final LinkMapper linkMapper;
    private final TagMapper tagMapper;
    private final FilterMapper filterMapper;

    public Subscription toDomain(SubscriptionEntity entity) {
        Subscription sub = Subscription.builder()
            .subscriptionId(new SubscriptionId(entity.id()))
            .user(userMapper.toDomain(entity.user()))
            .link(linkMapper.toDomain(entity.link()))
            .createdAt(entity.createdAt())
            .build();

        // добавить теги и фильтры
        List<Tag> tags = entity.tags()
            .stream()
            .map(tagMapper::toDomain)
            .toList();
        sub.tags(tags);

        List<Filter> filters = entity.filters()
            .stream()
            .map(filterMapper::toDomain)
            .toList();
        sub.filters(filters);

        return sub;
    }

    public SubscriptionEntity toEntity(Subscription domain) {
        SubscriptionEntity entity = new SubscriptionEntity();
        if (domain.subscriptionId() != null && domain.subscriptionId().id() != 0) {
            entity.id(domain.subscriptionId().id());
        }
        entity.user(userMapper.toEntity(domain.user()));
        entity.link(linkMapper.toEntity(domain.link()));
        entity.tags(
            domain.tags()
                .stream()
                .map(tagMapper::toEntity)
                .toList());
        entity.filters(
            domain.filters()
                .stream()
                .map(filterMapper::toEntity)
                .toList()
        );
        entity.createdAt(domain.createdAt());

        return entity;
    }
}
