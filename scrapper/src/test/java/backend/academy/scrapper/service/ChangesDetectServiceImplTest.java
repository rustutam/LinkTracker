package backend.academy.scrapper.service;


import backend.academy.scrapper.models.Link;
import backend.academy.scrapper.repository.api.GitHubExternalDataRepository;
import backend.academy.scrapper.repository.api.StackOverflowExternalDataRepository;
import backend.academy.scrapper.repository.database.LinksRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangesDetectServiceImplTest {

    @Mock
    private LinksRepository repository;

    @Mock
    private GitHubExternalDataRepository gitHubExternalDataRepository;

    @Mock
    private StackOverflowExternalDataRepository stackOverflowExternalDataRepository;

    @InjectMocks
    private ChangesDetectServiceImpl changesDetectService;

    @Test
    @DisplayName("Должно вернуть пустой список, если в базе нет ссылок")
    void detectChanges_ShouldReturnEmptyList_WhenNoLinksInDatabase() {
        when(repository.getGitHubLinks()).thenReturn(List.of());
        when(repository.getStackOverflowLinks()).thenReturn(List.of());

        List<Link> result = changesDetectService.detectChanges();

        assertTrue(result.isEmpty());

        verify(repository).getGitHubLinks();
        verify(repository).getStackOverflowLinks();
    }

    @Test
    @DisplayName("Должно вернуть пустой список, если нет обновлений на GitHub и StackOverflow")
    void detectChanges_ShouldReturnEmptyList_WhenNoUpdatesOnGitHubAndStackOverflow() {
        OffsetDateTime dateTime = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime lastUpdateTime = OffsetDateTime.of(2019, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);


        List<Link> gitHubLinks = List.of(
            new Link(1L, URI.create("https://github.com/user/repo"), List.of(), List.of(), dateTime)
        );
        List<Link> stackOverflowLinks = List.of(
            new Link(2L, URI.create("https://stackoverflow.com/q/12345"), List.of(), List.of(), dateTime)
        );

        when(repository.getGitHubLinks()).thenReturn(gitHubLinks);
        when(repository.getStackOverflowLinks()).thenReturn(stackOverflowLinks);
        when(gitHubExternalDataRepository.getLastUpdateDates(gitHubLinks))
            .thenReturn(Map.of(1L, lastUpdateTime));
        when(stackOverflowExternalDataRepository.getLastUpdateDates(stackOverflowLinks))
            .thenReturn(Map.of(2L, lastUpdateTime));

        List<Link> result = changesDetectService.detectChanges();

        assertTrue(result.isEmpty());

        verify(repository).getGitHubLinks();
        verify(repository).getStackOverflowLinks();
    }

    @Test
    @DisplayName("Должно вернуть список обновлённых ссылок с GitHub")
    void detectChanges_ShouldReturnUpdatedGitHubLinks_WhenThereAreUpdates() {
        OffsetDateTime newTime = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime oldTime = newTime.minusDays(1);

        List<Link> gitHubLinks = List.of(
            new Link(1L, URI.create("https://github.com/user/repo"), List.of(), List.of(), oldTime)
        );

        when(repository.getGitHubLinks()).thenReturn(gitHubLinks);
        when(repository.getStackOverflowLinks()).thenReturn(List.of());
        when(gitHubExternalDataRepository.getLastUpdateDates(gitHubLinks))
            .thenReturn(Map.of(1L, newTime));
        when(stackOverflowExternalDataRepository.getLastUpdateDates(List.of()))
            .thenReturn(Map.of());

        List<Link> result = changesDetectService.detectChanges();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());

        verify(repository).updateLastUpdateTime(1L, newTime);
    }

    @Test
    @DisplayName("Должно вернуть список обновлённых ссылок с StackOverflow")
    void detectChanges_ShouldReturnUpdatedStackOverflowLinks_WhenThereAreUpdates() {
        OffsetDateTime newTime = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime oldTime = newTime.minusDays(1);

        List<Link> stackOverflowLinks = List.of(
            new Link(1L, URI.create("https://github.com/user/repo"), List.of(), List.of(), oldTime)
        );

        when(repository.getGitHubLinks()).thenReturn(List.of());
        when(repository.getStackOverflowLinks()).thenReturn(stackOverflowLinks);
        when(gitHubExternalDataRepository.getLastUpdateDates(List.of()))
            .thenReturn(Map.of());
        when(stackOverflowExternalDataRepository.getLastUpdateDates(stackOverflowLinks))
            .thenReturn(Map.of(1L, newTime));

        List<Link> result = changesDetectService.detectChanges();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());

        verify(repository).updateLastUpdateTime(1L, newTime);
    }

    @Test
    @DisplayName("Должно вернуть объединённый список обновлённых ссылок с GitHub и StackOverflow")
    void detectChanges_ShouldReturnCombinedList_WhenThereAreUpdatesOnBothPlatforms() {
        OffsetDateTime newTime = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime oldTime = newTime.minusDays(1);

        List<Link> gitHubLinks = List.of(
            new Link(1L, URI.create("https://github.com/user/repo"), List.of(), List.of(), oldTime)
        );
        List<Link> stackOverflowLinks = List.of(
            new Link(2L, URI.create("https://stackoverflow.com/q/12345"), List.of(), List.of(), oldTime)
        );

        when(repository.getGitHubLinks()).thenReturn(gitHubLinks);
        when(repository.getStackOverflowLinks()).thenReturn(stackOverflowLinks);
        when(gitHubExternalDataRepository.getLastUpdateDates(gitHubLinks))
            .thenReturn(Map.of(1L, newTime));
        when(stackOverflowExternalDataRepository.getLastUpdateDates(stackOverflowLinks))
            .thenReturn(Map.of(2L, newTime));

        List<Link> result = changesDetectService.detectChanges();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());

        verify(repository).updateLastUpdateTime(1L, newTime);
    }
}

