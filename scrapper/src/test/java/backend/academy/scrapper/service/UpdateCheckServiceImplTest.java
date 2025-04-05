//package backend.academy.scrapper.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import backend.academy.scrapper.models.LinkMetadata;
//import backend.academy.scrapper.repository.api.GitHubExternalDataRepository;
//import backend.academy.scrapper.repository.api.StackOverflowExternalDataRepository;
//import backend.academy.scrapper.repository.database.LinksRepository;
//import java.net.URI;
//import java.time.OffsetDateTime;
//import java.time.ZoneOffset;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class UpdateCheckServiceImplTest {
//
//    @Mock
//    private LinksRepository repository;
//
//    @Mock
//    private GitHubExternalDataRepository gitHubExternalDataRepository;
//
//    @Mock
//    private StackOverflowExternalDataRepository stackOverflowExternalDataRepository;
//
//    @InjectMocks
//    private UpdateCheckServiceImpl changesDetectService;
//
//    private final OffsetDateTime newTime = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
//    private final OffsetDateTime oldTime = newTime.minusDays(1);
//
//    @Test
//    @DisplayName("Должно вернуть пустой список, если в базе нет ссылок")
//    void detectChanges_ShouldReturnEmptyList_WhenNoLinksInDatabase() {
//        when(repository.getGitHubLinks()).thenReturn(List.of());
//        when(repository.getStackOverflowLinks()).thenReturn(List.of());
//
//        List<LinkMetadata> result = changesDetectService.detectChanges();
//
//        assertTrue(result.isEmpty());
//
//        verify(repository).getGitHubLinks();
//        verify(repository).getStackOverflowLinks();
//    }
//
//    @Test
//    @DisplayName("Должно вернуть пустой список, если нет обновлений на GitHub и StackOverflow")
//    void detectChanges_ShouldReturnEmptyList_WhenNoUpdatesOnGitHubAndStackOverflow() {
//        List<LinkMetadata> gitHubLinksBeforeUpdate = List.of(
//                new LinkMetadata(1L, URI.create("https://github.com/user/repo"), oldTime),
//                new LinkMetadata(2L, URI.create("https://github.com/user/repo"), oldTime),
//                new LinkMetadata(3L, URI.create("https://github.com/user/repo"), oldTime));
//
//        List<LinkMetadata> gitHubLinksAfterUpdate = List.of(
//                new LinkMetadata(1L, URI.create("https://github.com/user/repo"), oldTime),
//                new LinkMetadata(2L, URI.create("https://github.com/user/repo"), oldTime),
//                new LinkMetadata(3L, URI.create("https://github.com/user/repo"), oldTime));
//        List<LinkMetadata> stackOverflowLinksBeforeUpdate =
//                List.of(new LinkMetadata(4L, URI.create("https://stackoverflow.com/q/12345"), oldTime));
//        List<LinkMetadata> stackOverflowLinksAfterUpdate =
//                List.of(new LinkMetadata(4L, URI.create("https://stackoverflow.com/q/12345"), oldTime));
//
//        when(repository.getGitHubLinks()).thenReturn(gitHubLinksBeforeUpdate);
//        when(repository.getStackOverflowLinks()).thenReturn(stackOverflowLinksBeforeUpdate);
//        when(gitHubExternalDataRepository.getLinksWithNewLastUpdateDates(gitHubLinksBeforeUpdate))
//                .thenReturn(gitHubLinksAfterUpdate);
//        when(stackOverflowExternalDataRepository.getLinksWithNewLastUpdateDates(stackOverflowLinksBeforeUpdate))
//                .thenReturn(stackOverflowLinksAfterUpdate);
//
//        List<LinkMetadata> updatedLinks = changesDetectService.detectChanges();
//
//        assertTrue(updatedLinks.isEmpty());
//    }
//
//    @Test
//    @DisplayName("Должно вернуть список обновлённых ссылок с GitHub")
//    void detectChanges_ShouldReturnUpdatedGitHubLinks_WhenThereAreUpdates() {
//        List<LinkMetadata> gitHubLinksBeforeUpdate = List.of(
//                new LinkMetadata(1L, URI.create("https://github.com/123"), oldTime),
//                new LinkMetadata(2L, URI.create("https://github.com/345"), oldTime),
//                new LinkMetadata(3L, URI.create("https://github.com/567"), oldTime));
//
//        List<LinkMetadata> gitHubLinksAfterUpdate = List.of(
//                new LinkMetadata(1L, URI.create("https://github.com/123"), newTime),
//                new LinkMetadata(2L, URI.create("https://github.com/345"), newTime),
//                new LinkMetadata(3L, URI.create("https://github.com/567"), oldTime));
//
//        List<LinkMetadata> expectedUpdatedLinks = List.of(
//                new LinkMetadata(1L, URI.create("https://github.com/123"), newTime),
//                new LinkMetadata(2L, URI.create("https://github.com/345"), newTime));
//
//        when(repository.getGitHubLinks()).thenReturn(gitHubLinksBeforeUpdate);
//        when(gitHubExternalDataRepository.getLinksWithNewLastUpdateDates(gitHubLinksBeforeUpdate))
//                .thenReturn(gitHubLinksAfterUpdate);
//
//        List<LinkMetadata> updatedLinks = changesDetectService.detectChanges();
//
//        assertEquals(2, updatedLinks.size());
//        assertTrue(expectedUpdatedLinks.containsAll(updatedLinks) && updatedLinks.containsAll(expectedUpdatedLinks));
//    }
//
//    @Test
//    @DisplayName("Должно вернуть список обновлённых ссылок с StackOverflow")
//    void detectChanges_ShouldReturnUpdatedStackOverflowLinks_WhenThereAreUpdates() {
//        List<LinkMetadata> stackOverflowLinksBeforeUpdate = List.of(
//                new LinkMetadata(4L, URI.create("https://stackoverflow.com/q/12345"), oldTime),
//                new LinkMetadata(5L, URI.create("https://stackoverflow.com/q/123345"), oldTime));
//        List<LinkMetadata> stackOverflowLinksAfterUpdate = List.of(
//                new LinkMetadata(4L, URI.create("https://stackoverflow.com/q/12345"), newTime),
//                new LinkMetadata(5L, URI.create("https://stackoverflow.com/q/123345"), oldTime));
//
//        List<LinkMetadata> expectedUpdatedLinks =
//                List.of(new LinkMetadata(4L, URI.create("https://stackoverflow.com/q/12345"), newTime));
//
//        when(repository.getStackOverflowLinks()).thenReturn(stackOverflowLinksBeforeUpdate);
//        when(stackOverflowExternalDataRepository.getLinksWithNewLastUpdateDates(stackOverflowLinksBeforeUpdate))
//                .thenReturn(stackOverflowLinksAfterUpdate);
//
//        List<LinkMetadata> updatedLinks = changesDetectService.detectChanges();
//
//        assertEquals(1, updatedLinks.size());
//        assertEquals(updatedLinks, expectedUpdatedLinks);
//    }
//
//    @Test
//    @DisplayName("Должно вернуть объединённый список обновлённых ссылок с GitHub и StackOverflow")
//    void detectChanges_ShouldReturnCombinedList_WhenThereAreUpdatesOnBothPlatforms() {
//        List<LinkMetadata> gitHubLinksBeforeUpdate = List.of(
//                new LinkMetadata(1L, URI.create("https://github.com/user/repo/1"), oldTime),
//                new LinkMetadata(2L, URI.create("https://github.com/user/repo/2"), oldTime),
//                new LinkMetadata(3L, URI.create("https://github.com/user/repo/3"), oldTime));
//        List<LinkMetadata> gitHubLinksAfterUpdate = List.of(
//                new LinkMetadata(1L, URI.create("https://github.com/user/repo/1"), newTime),
//                new LinkMetadata(2L, URI.create("https://github.com/user/repo/2"), newTime),
//                new LinkMetadata(3L, URI.create("https://github.com/user/repo/3"), newTime));
//        List<LinkMetadata> stackOverflowLinksBeforeUpdate = List.of(
//                new LinkMetadata(4L, URI.create("https://stackoverflow.com/q/1"), oldTime),
//                new LinkMetadata(5L, URI.create("https://stackoverflow.com/q/2"), oldTime));
//        List<LinkMetadata> stackOverflowLinksAfterUpdate = List.of(
//                new LinkMetadata(4L, URI.create("https://stackoverflow.com/q/1"), newTime),
//                new LinkMetadata(5L, URI.create("https://stackoverflow.com/q/2"), oldTime));
//        List<LinkMetadata> expectedUpdatedLinks = List.of(
//                new LinkMetadata(1L, URI.create("https://github.com/user/repo/1"), newTime),
//                new LinkMetadata(2L, URI.create("https://github.com/user/repo/2"), newTime),
//                new LinkMetadata(3L, URI.create("https://github.com/user/repo/3"), newTime),
//                new LinkMetadata(4L, URI.create("https://stackoverflow.com/q/1"), newTime));
//
//        when(repository.getGitHubLinks()).thenReturn(gitHubLinksBeforeUpdate);
//        when(repository.getStackOverflowLinks()).thenReturn(stackOverflowLinksBeforeUpdate);
//        when(gitHubExternalDataRepository.getLinksWithNewLastUpdateDates(gitHubLinksBeforeUpdate))
//                .thenReturn(gitHubLinksAfterUpdate);
//        when(stackOverflowExternalDataRepository.getLinksWithNewLastUpdateDates(stackOverflowLinksBeforeUpdate))
//                .thenReturn(stackOverflowLinksAfterUpdate);
//
//        List<LinkMetadata> updatedLinks = changesDetectService.detectChanges();
//
//        assertEquals(4, updatedLinks.size());
//        assertTrue(expectedUpdatedLinks.containsAll(updatedLinks) && updatedLinks.containsAll(expectedUpdatedLinks));
//    }
//}
