package backend.academy.scrapper.service;

import backend.academy.scrapper.exceptions.AlreadyTrackLinkException;
import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.repository.database.LinksRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LinkServiceImplTest {

    @Mock
    private LinksRepository linksRepository;

    @InjectMocks
    private LinkServiceImpl linkService;

    @Test
    @DisplayName("Выбросить NotExistTgChatException, если чат не зарегистрирован")
    void addLink_ShouldThrowNotExistTgChatException_WhenChatNotRegistered() {
        long chatId = 123L;
        Link link = new Link(
            0,
            URI.create("https://example.com"),
            List.of(),
            List.of(),
            null);

        when(linksRepository.isRegistered(chatId)).thenReturn(false);

        assertThrows(NotExistTgChatException.class, () -> linkService.addLink(chatId, link));

        verify(linksRepository, never()).saveLink(anyLong(), any(Link.class));
    }

    @Test
    @DisplayName("Выбросить AlreadyTrackLinkException, если ссылка уже отслеживается")
    void addLink_ShouldThrowAlreadyTrackLinkException_WhenLinkAlreadyTracked() {
        long chatId = 123L;
        Link link = new Link(
            0,
            URI.create("https://example.com"),
            List.of(),
            List.of(),
            null);

        List<Link> existingLinks = List.of(
            new Link(
                0,
                URI.create("https://example.com"),
                List.of(),
                List.of(),
                null
            )
        );
        when(linksRepository.isRegistered(chatId)).thenReturn(true);
        when(linksRepository.findById(chatId)).thenReturn(existingLinks);

        assertThrows(AlreadyTrackLinkException.class, () -> linkService.addLink(chatId, link));

        verify(linksRepository, never()).saveLink(anyLong(), any(Link.class));
    }

    @Test
    @DisplayName("Сохранить ссылку, если её нет в списке отслеживаемых")
    void addLink_ShouldSaveLink_WhenNotAlreadyTracked() {
        long chatId = 123L;
        Link link = new Link(
            0,
            URI.create("https://example.com"),
            List.of(),
            List.of(),
            null);

        List<Link> existingLinks = List.of(
            new Link(
                0,
                URI.create("https://tbank.com"),
                List.of(),
                List.of(),
                null
            )
        );

        Link expectedLink = new Link(
            0,
            URI.create("https://example.com"),
            List.of(),
            List.of(),
            OffsetDateTime.now());

        when(linksRepository.isRegistered(chatId)).thenReturn(true);
        when(linksRepository.findById(chatId)).thenReturn(existingLinks);
        when(linksRepository.saveLink(eq(chatId), any(Link.class))).thenReturn(expectedLink);

        Link result = linkService.addLink(chatId, link);

        assertNotNull(result);
        assertEquals(expectedLink.uri(), result.uri());
        assertNotNull(result.lastUpdateTime());

        verify(linksRepository, times(1)).saveLink(eq(chatId), any(Link.class));
    }
}

