package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.UpdatedLink;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.repository.database.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class LinkProcessingServiceImplTest {

    private LinkRepository linkRepository;
    private OutboxPersistenceService outboxPersistenceService;
    private LinkUpdateDetectionService linkUpdateDetectionService;

    private LinkProcessingServiceImpl linkProcessingService;

    @BeforeEach
    void setUp() {
        linkRepository = mock(LinkRepository.class);
        outboxPersistenceService = mock(OutboxPersistenceService.class);
        linkUpdateDetectionService = mock(LinkUpdateDetectionService.class);

        linkProcessingService = new LinkProcessingServiceImpl(
            linkRepository,
            outboxPersistenceService,
            linkUpdateDetectionService
        );
    }

    @Test
    void processLinks_shouldProcessDetectedUpdates() {
        // given
        OffsetDateTime startTime = OffsetDateTime.now();
        int limit = 1;

        LinkId linkId = new LinkId(1L);
        Link link = new Link(linkId, URI.create("https://example.com"), OffsetDateTime.now(), OffsetDateTime.now());

        UpdatedLink updatedLink = UpdatedLink.builder()
            .id(linkId)
            .uri(link.uri())
            .description("Обновление найдено")
            .chatIds(List.of(new ChatId(100L)))
            .build();

        when(linkRepository.findOldestLinks(limit)).thenReturn(List.of(link));
        when(linkUpdateDetectionService.getUpdatedLinks(List.of(link))).thenReturn(List.of(updatedLink));

        // when
        linkProcessingService.processLinks(limit, startTime);

        // then
        verify(linkRepository).findOldestLinks(limit);
        verify(linkUpdateDetectionService).getUpdatedLinks(List.of(link));
        verify(outboxPersistenceService).process(updatedLink, startTime);
    }

    @Test
    void processLinks_shouldHandleNoLinks() {
        // given
        when(linkRepository.findOldestLinks(10)).thenReturn(List.of());

        // when
        linkProcessingService.processLinks(10, OffsetDateTime.now());

        // then
        verify(linkRepository).findOldestLinks(10);
        verify(linkUpdateDetectionService).getUpdatedLinks(any());
        verify(outboxPersistenceService, never()).process(any(), any());
    }
}
