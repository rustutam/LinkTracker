package backend.academy.scrapper.repository.database.jpa.mapper;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.entity.LinkEntity;
import java.net.URI;

public class LinkMapper {
    public static Link map(LinkEntity linkEntity) {
        return new Link(
            new LinkId(linkEntity.id()),
            URI.create(linkEntity.uri()),
            linkEntity.lastModifiedDate()
        );
    }
}
