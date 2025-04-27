package backend.academy.scrapper.repository.database.jdbc.mapper;

import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.models.dto.TagDto;

import java.util.List;

public class TagMapper {
    public static Tag toDomain(TagDto entity) {
        return new Tag(
            new TagId(entity.id()),
            entity.value()
        );
    }

    public static List<Tag> toDomain(List<TagDto> tagEntities) {
        return tagEntities.stream()
            .map(TagMapper::toDomain)
            .toList();
    }
}
