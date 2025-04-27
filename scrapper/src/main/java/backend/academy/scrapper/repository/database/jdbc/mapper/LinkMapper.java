package backend.academy.scrapper.repository.database.jdbc.mapper;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.dto.LinkDto;
import java.net.URI;

public class LinkMapper {
    public static Link toDomain(LinkDto entity) {
        return new Link(
            new LinkId(entity.id()),
            URI.create(entity.uri()),
            entity.lastModifiedDate()
        );
    }

    public static LinkDto toEntity(Link domain) {
        return new LinkDto(
            domain.linkId().id(),
            domain.uri().toString(),
            domain.lastUpdateTime(),
            null
        );
    }
}
