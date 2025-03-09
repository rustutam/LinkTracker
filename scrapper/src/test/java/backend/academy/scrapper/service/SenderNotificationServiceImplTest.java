package backend.academy.scrapper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.scrapper.models.LinkMetadata;
import backend.academy.scrapper.models.LinkUpdateNotification;
import backend.academy.scrapper.repository.database.LinksRepository;
import backend.academy.scrapper.sender.Sender;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SenderNotificationServiceImplTest {

    @Mock
    private LinksRepository linkRepository;

    @Mock
    private Sender sender;

    @InjectMocks
    private SenderNotificationServiceImpl senderNotificationService;

    @Test
    @DisplayName("Должен отправлять уведомления, если ссылки были обновлены")
    void notifySender_ShouldSendNotification_WhenLinksUpdated() {
        // Arrange
        OffsetDateTime time = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

        String exampleUri = "https://example.com";
        String gitUri = "https://github.com";
        List<LinkMetadata> updatedLinks = List.of(
                new LinkMetadata(1L, URI.create(exampleUri), time), new LinkMetadata(2L, URI.create(gitUri), time));

        when(linkRepository.getAllChatIdByLink(exampleUri)).thenReturn(List.of(101L, 102L));
        when(linkRepository.getAllChatIdByLink(gitUri)).thenReturn(List.of(103L, 104L, 105L));

        // Act
        senderNotificationService.notifySender(updatedLinks);

        // Assert
        ArgumentCaptor<List<LinkUpdateNotification>> captor = ArgumentCaptor.forClass(List.class);
        verify(sender).send(captor.capture());

        List<LinkUpdateNotification> notifications = captor.getValue();
        assertEquals(2, notifications.size());

        assertEquals(URI.create(exampleUri), notifications.get(0).uri());
        assertEquals(List.of(101L, 102L), notifications.get(0).chatIds());

        assertEquals(URI.create(gitUri), notifications.get(1).uri());
        assertEquals(List.of(103L, 104L, 105L), notifications.get(1).chatIds());
    }

    @Test
    @DisplayName("Не должен отправлять уведомления, если обновленных ссылок нет")
    void notifySender_ShouldNotSendNotification_WhenNoLinksUpdated() {
        // Arrange
        List<LinkMetadata> updatedLinks = List.of();

        // Act
        senderNotificationService.notifySender(updatedLinks);

        // Assert
        verify(sender, never()).send(any());
    }
}
