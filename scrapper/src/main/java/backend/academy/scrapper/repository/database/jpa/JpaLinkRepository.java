package backend.academy.scrapper.repository.database.jpa;

import backend.academy.scrapper.exceptions.NotExistLinkException;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.entity.LinkEntity;
import backend.academy.scrapper.repository.database.LinkRepository;
import backend.academy.scrapper.repository.database.jpa.mapper.LinkMapper;
import backend.academy.scrapper.repository.database.jpa.repo.LinkRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "ORM")
public class JpaLinkRepository implements LinkRepository {

    private final LinkRepo linkRepo;

    @Override
    public List<Link> findAll() {
        return linkRepo.findAll().stream()
            .map(LinkMapper::map)
            .toList();
    }

    @Override
    public Optional<Link> findById(LinkId linkId) {
        return linkRepo.findById(linkId.id())
            .map(LinkMapper::map);
    }

    @Override
    public Optional<Link> findByUri(URI uri) {
        return linkRepo.findByUri(uri.toString())
            .map(LinkMapper::map);
    }

    @Override
    @Transactional
    public Link updateLastUpdateTime(LinkId id, OffsetDateTime newLastModifyingTime) throws NotExistLinkException {
        LinkEntity entity = linkRepo.findById(id.id())
            .orElseThrow(NotExistLinkException::new);
        entity.lastModifiedDate(newLastModifyingTime);
        return LinkMapper.map(entity);
    }

    @Override
    public Link save(URI uri) {
        LinkEntity linkEntity = new LinkEntity();
        linkEntity.uri(uri.toString());
        return LinkMapper.map(linkRepo.save(linkEntity));
    }

    @Override
    public Page<Link> findAllPaginated(Pageable pageable) {
        return linkRepo.findAll(pageable)
            .map(LinkMapper::map);
    }
}
