package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.exceptions.NotExistLinkException;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.ids.LinkId;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LinkRepository {
    List<Link> findAll();

    Optional<Link> findById(LinkId linkId);

    Optional<Link> findByUri(URI uri);

    Link updateLastUpdateTime(LinkId id, OffsetDateTime newLastModifyingTime) throws NotExistLinkException;

    Link save(URI uri);

    Page<Link> findAllPaginated(Pageable pageable);
}
