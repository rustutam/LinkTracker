package backend.academy.scrapper.repository.database.jpa.mapper;

import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.models.entity.TagEntity;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    public Tag toDomain(TagEntity entity) {
        return new Tag(new TagId(entity.id()), entity.tag(), entity.createdAt());
    }

    public TagEntity toEntity(Tag domain) {
        TagEntity entity = new TagEntity();
        if (domain.tagId() != null && domain.tagId().id() != 0) {
            entity.id(domain.tagId().id());
        }
        entity.tag(domain.value());
        entity.createdAt(domain.createdAt());
        return entity;
    }
}
