package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.UpdatedLink;
import backend.academy.scrapper.repository.database.LinkRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkProcessingServiceImpl implements LinkProcessingService {
    private final LinkRepository linkRepository;
    private final OutboxPersistenceService outboxPersistenceService;
    private final LinkUpdateDetectionService linkUpdateDetectionService;

    @Override
    public void processLinks(Integer limit, OffsetDateTime updateStartTime) {

        List<Link> oldestLinks = linkRepository.findOldestLinks(limit);

        List<UpdatedLink> updatedLinks = linkUpdateDetectionService.getUpdatedLinks(oldestLinks);

        updatedLinks.forEach(updatedLink -> {
            outboxPersistenceService.process(updatedLink, updateStartTime);
            log.atInfo()
                    .addKeyValue("link", updatedLink.uri().toString())
                    .setMessage("Найдено обновление для ссылки")
                    .log();
        });
    }
}
