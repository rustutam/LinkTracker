package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.models.entities.RepositoryTables;
import backend.academy.scrapper.models.entities.UserLinksEntity;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LinksRepositoryImplTest {
    @Mock
    private RepositoryTables repositoryTables;

    @InjectMocks
    private LinksRepositoryImpl linksRepository;

    private final long chatId = 1L;
    private final long chatId2 = 2L;
    private final URI testUri = URI.create("https://github.com/test");
    private Link testLink;
    private Link testLink2;
    private final OffsetDateTime time = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);


    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("when chat is registered then isRegistered true")
    void isRegisteredTest() {
        when(repositoryTables.users()).thenReturn(List.of(1L,2L,3L));

        boolean registered = linksRepository.isRegistered(1L);

        assertTrue(registered);
    }

    @Test
    @DisplayName("when chat is not registered then isRegistered false")
    void isNotRegisteredTest() {
        when(repositoryTables.users()).thenReturn(List.of(1L,2L,3L));

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
        userLinksEntities.add(new UserLinksEntity(1L,1L,1L, 1L));
        userLinksEntities.add(new UserLinksEntity(2L,1L,2L, 2L));
        userLinksEntities.add(new UserLinksEntity(3L,2L,2L, 3L));
        userLinksEntities.add(new UserLinksEntity(4L,2L,4L, 4L));


        when(repositoryTables.users()).thenReturn(users);
        when( repositoryTables.userLinksEntities()).thenReturn(userLinksEntities);

        linksRepository.unRegister(1L);

        assertFalse(users.contains(1L));
        assertFalse(userLinksEntities.stream()
                .anyMatch(entity -> entity.chatId() == 1L));
    }


//
//    @Test
//    @DisplayName("Должен сохранять ссылку и присваивать ID")
//    void shouldSaveLinkAndAssignId() {
//
//
//        Link savedLink = linksRepository.saveLink(chatId, testLink);
//
//        assertTrue(linksRepository.findById(chatId).contains(savedLink));
//        assertTrue(savedLink.id() > 0);
//    }
//
//    @Test
//    @DisplayName("Должен удалять ссылку")
//    void shouldDeleteLink() {
//        linksRepository.saveLink(chatId, testLink);
//        Optional<Link> deletedLink = linksRepository.deleteLink(chatId, testUri.toString());
//        assertTrue(deletedLink.isPresent());
//        assertTrue(linksRepository.findById(chatId).isEmpty());
//    }

//    @Test
//    @DisplayName("Должен находить ссылки по chatId")
//    void shouldFindLinksByChatId() {
//        linksRepository.saveLink(chatId, testLink);
//
//        List<Link> links = linksRepository.findById(chatId);
//        assertThat(links).hasSize(1);
//        assertThat(links.get(0).uri()).isEqualTo(testUri);
//    }
//
//    @Test
//    @DisplayName("Должен возвращать список чатов по ссылке")
//    void shouldReturnChatIdsByLink() {
//        linksRepository.saveLink(chatId, testLink);
//        List<Long> chatIds = linksRepository.getAllChatIdByLink(testUri.toString());
//        assertThat(chatIds).contains(chatId);
//    }
//
//    @Test
//    @DisplayName("Должен обновлять время последнего обновления")
//    void shouldUpdateLastUpdateTime() {
//        Link savedLink = linksRepository.saveLink(chatId, testLink);
//        linksRepository.saveLink(chatId2, testLink2);
//
//        OffsetDateTime newTime = time.plusDays(1);
//        linksRepository.updateLinksLastUpdateTime(savedLink.id(), newTime);
//        Link savedLinkAfterUpdate = linksRepository.findById(chatId).get(0);
//        Link savedLinkAfterUpdate2 = linksRepository.findById(chatId2).get(0);
//
//        assertEquals(savedLinkAfterUpdate.lastUpdateTime(), newTime);
//        assertEquals(savedLinkAfterUpdate2.lastUpdateTime(), newTime);
//    }
}
