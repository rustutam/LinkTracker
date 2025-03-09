package backend.academy.scrapper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.scrapper.exceptions.AlreadyTrackLinkException;
import backend.academy.scrapper.exceptions.NotExistTgChatException;
import backend.academy.scrapper.exceptions.NotTrackLinkException;
import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.repository.api.GitHubExternalDataRepository;
import backend.academy.scrapper.repository.database.LinksRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LinkServiceImplTest {

    @Mock
    private LinksRepository linksRepository;

    @Mock
    private GitHubExternalDataRepository gitHubExternalDataRepository;

    @InjectMocks
    private LinkServiceImpl linkService;

    @Test
    @DisplayName("Выбросить NotExistTgChatException, если чат не зарегистрирован")
    void addLink_ShouldThrowNotExistTgChatException_WhenChatNotRegistered() {
        long chatId = 123L;
        Link link = new Link(0, URI.create("https://github.com/repo"), List.of(), List.of(), null);

        when(linksRepository.isRegistered(chatId)).thenReturn(false);
        when(gitHubExternalDataRepository.isProcessingUri(any(URI.class))).thenReturn(true);
        when(gitHubExternalDataRepository.getLastUpdateDate(any(URI.class))).thenReturn(OffsetDateTime.now());

        assertThrows(NotExistTgChatException.class, () -> linkService.addLink(chatId, link));

        verify(linksRepository, never()).saveLink(anyLong(), any(Link.class));
    }

    @Test
    @DisplayName("Выбросить AlreadyTrackLinkException, если ссылка уже отслеживается")
    void addLink_ShouldThrowAlreadyTrackLinkException_WhenLinkAlreadyTracked() {
        long chatId = 123L;
        Link link = new Link(0, URI.create("https://github.com/repo"), List.of(), List.of(), null);

        List<Link> existingLinks =
                List.of(new Link(0, URI.create("https://github.com/repo"), List.of(), List.of(), null));
        when(linksRepository.isRegistered(chatId)).thenReturn(true);
        when(linksRepository.findById(chatId)).thenReturn(existingLinks);
        when(gitHubExternalDataRepository.isProcessingUri(any(URI.class))).thenReturn(true);
        when(gitHubExternalDataRepository.getLastUpdateDate(any(URI.class))).thenReturn(OffsetDateTime.now());

        assertThrows(AlreadyTrackLinkException.class, () -> linkService.addLink(chatId, link));

        verify(linksRepository, never()).saveLink(anyLong(), any(Link.class));
    }

    @Test
    @DisplayName("Сохранить ссылку, если её нет в списке отслеживаемых")
    void addLink_ShouldSaveLink_WhenNotAlreadyTracked() {
        long chatId = 123L;
        Link link = new Link(0, URI.create("https://stackoverflow.com/repo"), List.of(), List.of(), null);

        List<Link> existingLinks =
                List.of(new Link(0, URI.create("https://github.com/repo"), List.of(), List.of(), null));

        Link expectedLink =
                new Link(0, URI.create("https://stackoverflow.com/repo"), List.of(), List.of(), OffsetDateTime.now());

        when(linksRepository.isRegistered(chatId)).thenReturn(true);
        when(linksRepository.findById(chatId)).thenReturn(existingLinks);
        when(linksRepository.saveLink(eq(chatId), any(Link.class))).thenReturn(expectedLink);
        when(gitHubExternalDataRepository.isProcessingUri(any(URI.class))).thenReturn(true);
        when(gitHubExternalDataRepository.getLastUpdateDate(any(URI.class))).thenReturn(OffsetDateTime.now());

        Link result = linkService.addLink(chatId, link);

        assertNotNull(result);
        assertEquals(expectedLink.uri(), result.uri());
        assertNotNull(result.lastUpdateTime());

        verify(linksRepository, times(1)).saveLink(eq(chatId), any(Link.class));
    }

    @Test
    @DisplayName("Выбросить NotExistTgChatException, если чат не зарегистрирован")
    void removeLink_ShouldThrowNotExistTgChatException_WhenChatNotRegistered() {
        long chatId = 123L;
        String uri = "https://example.com";

        when(linksRepository.isRegistered(chatId)).thenReturn(false);

        assertThrows(NotExistTgChatException.class, () -> linkService.removeLink(chatId, uri));

        verify(linksRepository, never()).deleteLink(anyLong(), anyString());
    }

    @Test
    @DisplayName("Выбросить NotTrackLinkException, если ссылка не отслеживается")
    void removeLink_ShouldThrowNotTrackLinkException_WhenLinkNotTracked() {
        long chatId = 123L;
        String uri = "https://example.com";

        when(linksRepository.isRegistered(chatId)).thenReturn(true);
        when(linksRepository.deleteLink(chatId, uri)).thenReturn(Optional.empty());

        assertThrows(NotTrackLinkException.class, () -> linkService.removeLink(chatId, uri));

        verify(linksRepository).deleteLink(chatId, uri);
    }

    @Test
    @DisplayName("Вернуть удалённую ссылку, если она существовала")
    void removeLink_ShouldReturnDeletedLink_WhenLinkExists() {

        long chatId = 123L;
        String uri = "https://example.com";
        OffsetDateTime time = OffsetDateTime.now();
        Link deletedLink = new Link(0, URI.create(uri), List.of(), List.of(), time);

        when(linksRepository.isRegistered(chatId)).thenReturn(true);
        when(linksRepository.deleteLink(chatId, uri)).thenReturn(Optional.of(deletedLink));

        Link result = linkService.removeLink(chatId, uri);

        assertNotNull(result);
        assertEquals(deletedLink.uri(), result.uri());
        assertEquals(deletedLink.lastUpdateTime(), result.lastUpdateTime());

        verify(linksRepository).deleteLink(chatId, uri);
    }

    @Test
    @DisplayName("Выбросить NotExistTgChatException, если чат не зарегистрирован")
    void getLinks_ShouldThrowNotExistTgChatException_WhenChatNotRegistered() {
        long chatId = 123L;

        when(linksRepository.isRegistered(chatId)).thenReturn(false);

        assertThrows(NotExistTgChatException.class, () -> linkService.getLinks(chatId));

        verify(linksRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Вернуть пустой список, если у чата нет ссылок")
    void getLinks_ShouldReturnEmptyList_WhenNoLinksExist() {
        long chatId = 123L;

        when(linksRepository.isRegistered(chatId)).thenReturn(true);
        when(linksRepository.findById(chatId)).thenReturn(List.of());

        List<Link> result = linkService.getLinks(chatId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(linksRepository).findById(chatId);
    }

    @Test
    @DisplayName("Вернуть список ссылок, если они есть")
    void getLinks_ShouldReturnListOfLinks_WhenLinksExist() {
        long chatId = 123L;
        OffsetDateTime time = OffsetDateTime.now();
        List<Link> expectedLinks = List.of(
                new Link(1L, URI.create("https://example.com"), List.of(), List.of(), time),
                new Link(2L, URI.create("https://another.com"), List.of(), List.of(), time));

        when(linksRepository.isRegistered(chatId)).thenReturn(true);
        when(linksRepository.findById(chatId)).thenReturn(expectedLinks);

        List<Link> result = linkService.getLinks(chatId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedLinks, result);

        verify(linksRepository).findById(chatId);
    }
}
