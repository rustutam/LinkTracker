package backend.academy.scrapper.repository.database;


import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    List<Link> findAll();

    Optional<Link> findById(LinkId id);

    Optional<Link> findByUri(URI uri);

    Link updateLastModifying(LinkId id, OffsetDateTime newLastModifyingTime);

    Link save(URI uri);

    Page<Link> findAll(Pageable pageable);
}
