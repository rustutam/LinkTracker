package backend.academy.scrapper.repository.database.utilities.mapper;

import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.models.entities.TagEntity;

public class TagMapper {
    public static Tag toDomain(TagEntity entity) {
        return new Tag(
            new TagId(entity.id()),
            entity.value()
        );
    }
}
