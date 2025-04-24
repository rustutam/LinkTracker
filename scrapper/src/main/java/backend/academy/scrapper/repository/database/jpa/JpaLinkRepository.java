package backend.academy.scrapper.repository.database.jpa;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.entity.LinkEntity;
import backend.academy.scrapper.repository.database.LinkRepository;
import backend.academy.scrapper.repository.database.jpa.repo.LinkRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "ORM")
public class JpaLinkRepository{

    private final LinkRepo linkRepo;

    Optional<Link> findById(LinkId id) {
        Optional<LinkEntity> linkEntity = linkRepo.findById(id.id());
    }
}
