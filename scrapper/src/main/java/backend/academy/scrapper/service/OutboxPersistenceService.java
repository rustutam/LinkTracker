package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.UpdatedLink;
import backend.academy.scrapper.repository.database.LinkRepository;
import backend.academy.scrapper.repository.database.LinkUpdateRepository;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OutboxPersistenceService {
    private final LinkUpdateRepository linkUpdateRepository;
    private final LinkRepository linkRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void process(UpdatedLink updatedLink, OffsetDateTime updateStartTime) {
        linkUpdateRepository.save(updatedLink);
        linkRepository.updateLastUpdateTime(updatedLink.id(), updateStartTime);
    }
}
