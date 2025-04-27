package backend.academy.scrapper.repository.database.jdbc.mapper;

import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.dto.SubscriptionDto;
import java.util.List;

public class SubscriptionMapper {
    public static Subscription toDomain(SubscriptionDto entity) {
        User user = UserMapper.toDomain(entity.userDto());
        Link link = LinkMapper.toDomain(entity.linkDto());
        List<Tag> tags = TagMapper.toDomain(entity.tagsDto());
        List<Filter> filters = FilterRowMapper.toDomain(entity.filtersDto());


        return new Subscription(
            new SubscriptionId(entity.id()),
            user,
            link,
            tags,
            filters
        );

    }
}
