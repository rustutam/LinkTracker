package backend.academy.scrapper.service;

import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.models.LinkUpdateNotification;
import backend.academy.scrapper.repository.database.LinksRepository;
import backend.academy.scrapper.sender.Sender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        List<Link> updatedLinks = List.of(
            new Link(1L, URI.create("https://example.com"), List.of(), List.of(), time),
            new Link(2L, URI.create("https://github.com"), List.of(), List.of(), time)
        );

        when(linkRepository.getAllChatIdByLink("https://example.com"))
            .thenReturn(List.of(101L, 102L));
        when(linkRepository.getAllChatIdByLink("https://github.com"))
            .thenReturn(List.of(103L, 104L, 105L));

        // Act
        senderNotificationService.notifySender(updatedLinks);

        // Assert
        ArgumentCaptor<List<LinkUpdateNotification>> captor = ArgumentCaptor.forClass(List.class);
        verify(sender).send(captor.capture());

        List<LinkUpdateNotification> notifications = captor.getValue();
        assertEquals(2, notifications.size());

        assertEquals("https://example.com", notifications.get(0).uri());
        assertEquals(List.of(101L, 102L), notifications.get(0).chatIds());

        assertEquals("https://github.com", notifications.get(1).uri());
        assertEquals(List.of(103L, 104L, 105L), notifications.get(1).chatIds());
    }

    @Test
    @DisplayName("Не должен отправлять уведомления, если обновленных ссылок нет")
    void notifySender_ShouldNotSendNotification_WhenNoLinksUpdated() {
        // Arrange
        List<Link> updatedLinks = List.of();

        // Act
        senderNotificationService.notifySender(updatedLinks);

        // Assert
        verify(sender, never()).send(any());
    }
}

