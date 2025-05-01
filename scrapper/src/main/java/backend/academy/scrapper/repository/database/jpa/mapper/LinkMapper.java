package backend.academy.scrapper.repository.database.jpa.mapper;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.entity.LinkEntity;
import java.net.URI;
import org.springframework.stereotype.Component;

@Component
public class LinkMapper {
    public Link toDomain(LinkEntity entity) {
        return new Link(
                new LinkId(entity.id()), URI.create(entity.uri()), entity.lastModifiedDate(), entity.createdAt());
    }

    public LinkEntity toEntity(Link domain) {
        LinkEntity entity = new LinkEntity();
        if (domain.linkId() != null && domain.linkId().id() != 0) {
            entity.id(domain.linkId().id());
        }
        entity.uri(domain.uri().toString());
        entity.lastModifiedDate(domain.lastUpdateTime());
        entity.createdAt(domain.createdAt());
        return entity;
    }
}
