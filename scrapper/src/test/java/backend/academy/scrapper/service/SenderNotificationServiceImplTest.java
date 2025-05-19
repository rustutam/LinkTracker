package backend.academy.scrapper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import backend.academy.scrapper.models.domain.UpdatedLink;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.repository.database.LinkUpdateRepository;
import backend.academy.scrapper.sender.LinkUpdateSender;
import dto.LinkUpdate;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SenderNotificationServiceImplTest {

    private LinkUpdateSender linkUpdateSender;
    private LinkUpdateRepository linkUpdateRepository;
    private SenderNotificationServiceImpl senderNotificationService;

    @BeforeEach
    void setUp() {
        linkUpdateSender = mock(LinkUpdateSender.class);
        linkUpdateRepository = mock(LinkUpdateRepository.class);
        senderNotificationService = new SenderNotificationServiceImpl(linkUpdateSender, linkUpdateRepository);
    }

    @Test
    void notifySender_shouldSendUpdateAndDelete() {
        // given
        UpdatedLink updatedLink = UpdatedLink.builder()
                .id(new LinkId(1L))
                .uri(URI.create("https://example.com"))
                .description("Test update")
                .chatIds(List.of(new ChatId(100L), new ChatId(200L)))
                .build();

        // when
        senderNotificationService.notifySender(updatedLink);

        // then
        ArgumentCaptor<LinkUpdate> captor = ArgumentCaptor.forClass(LinkUpdate.class);
        verify(linkUpdateSender).sendUpdates(captor.capture());
        verify(linkUpdateRepository).deleteById(updatedLink.id());

        LinkUpdate sent = captor.getValue();
        assertEquals(sent.id(), 1L);
        assertEquals(sent.url(), "https://example.com");
        assertEquals(sent.description(), "Test update");
        assertEquals(sent.tgChatIds(), List.of(100L, 200L));
    }

    @Test
    void notifySender_shouldLogErrorAndNotDelete_ifSenderFails() {
        // given
        UpdatedLink updatedLink = UpdatedLink.builder()
                .id(new LinkId(2L))
                .uri(URI.create("https://example.com/fail"))
                .description("Failing update")
                .chatIds(List.of(new ChatId(300L)))
                .build();

        doThrow(new RuntimeException("Sender failure")).when(linkUpdateSender).sendUpdates(any(LinkUpdate.class));

        // when
        senderNotificationService.notifySender(updatedLink);

        // then
        verify(linkUpdateSender).sendUpdates(any(LinkUpdate.class));
        verify(linkUpdateRepository, never()).deleteById(any());
    }
}
