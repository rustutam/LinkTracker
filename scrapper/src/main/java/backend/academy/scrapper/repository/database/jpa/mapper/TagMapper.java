package backend.academy.scrapper.repository.database.jpa.mapper;

import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.models.entity.TagEntity;

public class TagMapper {
    public static Tag map(TagEntity tagEntity) {
        return new Tag(
            new TagId(tagEntity.id()),
            tagEntity.tag()
        );
    }
}
