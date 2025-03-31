package backend.academy.scrapper.repository.database.utilities.mapper;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.entities.LinkEntity;
import java.net.URI;

public class LinkMapper {
    public static Link toDomain(LinkEntity entity) {
        return new Link(
            new LinkId(entity.id()),
            URI.create(entity.uri()),
            entity.lastModifiedDate()
        );
    }

    public static LinkEntity toEntity(Link domain) {
        return new LinkEntity(
            domain.linkId().id(),
            domain.uri().toString(),
            domain.lastUpdateTime(),
            null
        );
    }
}
