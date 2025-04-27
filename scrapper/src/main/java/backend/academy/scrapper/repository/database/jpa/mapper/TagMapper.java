package backend.academy.scrapper.repository.database.jpa.mapper;

import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.models.entity.TagEntity;
import java.util.List;

public class TagMapper {
    public static Tag map(TagEntity tagEntity) {
        return new Tag(
            new TagId(tagEntity.id()),
            tagEntity.tag()
        );
    }

    public static List<Tag> map(List<TagEntity> tagEntities) {
        return tagEntities.stream()
            .map(TagMapper::map)
            .toList();
    }
}
