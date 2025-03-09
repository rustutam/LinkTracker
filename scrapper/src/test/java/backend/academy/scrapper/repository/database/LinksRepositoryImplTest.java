package backend.academy.scrapper.repository.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.models.entities.InfoEntity;
import backend.academy.scrapper.models.entities.LinksEntity;
import backend.academy.scrapper.models.entities.RepositoryTables;
import backend.academy.scrapper.models.entities.UserLinksEntity;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LinksRepositoryImplTest {
    @Mock
    private RepositoryTables repositoryTables;

    @InjectMocks
    private LinksRepositoryImpl linksRepository;

    private final List<LinksEntity> linksEntities = new ArrayList<>();
    private final List<UserLinksEntity> userLinksEntities = new ArrayList<>();
    private final List<InfoEntity> infoEntities = new ArrayList<>();

    @Test
    @DisplayName("when chat is registered then isRegistered true")
    void isRegisteredTest() {
        when(repositoryTables.users()).thenReturn(List.of(1L, 2L, 3L));

        boolean registered = linksRepository.isRegistered(1L);

        assertTrue(registered);
    }

    @Test
    @DisplayName("when chat is not registered then isRegistered false")
    void isNotRegisteredTest() {
        when(repositoryTables.users()).thenReturn(List.of(1L, 2L, 3L));

        boolean registered = linksRepository.isRegistered(4L);

        assertFalse(registered);
    }

    @Test
    @DisplayName("Должен корректно регистрировать чат")
    void shouldRegisterChat() {
        ArrayList<Long> users = new ArrayList<>();
        when(repositoryTables.users()).thenReturn(users);

        linksRepository.register(1L);

        assertTrue(users.contains(1L));
    }

    @Test
    @DisplayName("Должен корректно удалять регистрацию чата")
    void shouldUnregisterChat() {
        List<Long> users = new ArrayList<>();
        users.add(1L);
        users.add(2L);

        List<UserLinksEntity> userLinksEntities = new ArrayList<>();
        userLinksEntities.add(new UserLinksEntity(1L, 1L, 1L, 1L));
        userLinksEntities.add(new UserLinksEntity(2L, 1L, 2L, 2L));
        userLinksEntities.add(new UserLinksEntity(3L, 2L, 2L, 3L));
        userLinksEntities.add(new UserLinksEntity(4L, 2L, 4L, 4L));

        when(repositoryTables.users()).thenReturn(users);
        when(repositoryTables.userLinksEntities()).thenReturn(userLinksEntities);

        linksRepository.unRegister(1L);

        assertFalse(users.contains(1L));
        assertFalse(userLinksEntities.stream().anyMatch(entity -> entity.chatId() == 1L));
    }

    @Test
    void testSaveLink() {
        long chatId = 1L;
        URI testUri = URI.create("https://example.com");
        OffsetDateTime now = OffsetDateTime.now();
        Link testLink = new Link(0, testUri, List.of("tag1"), List.of("filter1"), now);

        when(repositoryTables.linksEntities()).thenReturn(linksEntities);
        when(repositoryTables.userLinksEntities()).thenReturn(userLinksEntities);
        when(repositoryTables.infoEntities()).thenReturn(infoEntities);

        linksRepository.register(chatId);
        Link savedLink = linksRepository.saveLink(chatId, testLink);

        assertEquals(savedLink.uri(), testUri);
        assertEquals(1, linksEntities.size());
        assertEquals(1, userLinksEntities.size());
    }

    @Test
    void testDeleteLink() {
        long chatId = 1L;
        URI testUri = URI.create("https://example.com");
        OffsetDateTime now = OffsetDateTime.now();
        linksEntities.add(new LinksEntity(1L, testUri, now));
        userLinksEntities.add(new UserLinksEntity(1L, chatId, 1L, 1L));
        infoEntities.add(new InfoEntity(1L, List.of("title"), List.of("description")));

        when(repositoryTables.linksEntities()).thenReturn(linksEntities);
        when(repositoryTables.userLinksEntities()).thenReturn(userLinksEntities);
        when(repositoryTables.infoEntities()).thenReturn(infoEntities);

        Optional<Link> deletedLink = linksRepository.deleteLink(chatId, testUri.toString());

        assertTrue(deletedLink.isPresent());
        assertTrue(userLinksEntities.isEmpty());
    }
}
