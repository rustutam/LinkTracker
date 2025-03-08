package backend.academy.scrapper.integration_tests;

import backend.academy.scrapper.handler.LinkHandler;
import backend.academy.scrapper.models.api.request.AddLinkRequest;
import backend.academy.scrapper.models.api.request.RemoveLinkRequest;
import backend.academy.scrapper.models.api.response.LinkResponse;
import backend.academy.scrapper.repository.api.GitHubExternalDataRepository;
import backend.academy.scrapper.repository.api.StackOverflowExternalDataRepository;
import backend.academy.scrapper.repository.database.LinksRepositoryImpl;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ControllerIntegrationTests {
    @Autowired
    private LinkHandler linkHandler;

    @MockBean
    private GitHubExternalDataRepository gitHubExternalDataRepository;


    @Autowired
    private LinksRepositoryImpl linksRepository;

    @Test
    public void testAddLink() {
        Long tgChatId = 1L;
        URI link = URI.create("https://github.com/Rustut/LinkTracker");
        AddLinkRequest addLinkRequest = new AddLinkRequest(link, List.of(), List.of());

        when(gitHubExternalDataRepository.isProcessingUri(any())).thenReturn(true);
        when(gitHubExternalDataRepository.getLastUpdateDate(any())).thenReturn(OffsetDateTime.now());

        // Call the addLink method
        linksRepository.register(tgChatId);
        LinkResponse linkResponse = linkHandler.addLink(tgChatId, addLinkRequest);

        // Verify that the link was saved in the database
        assertThat(linksRepository.findById(tgChatId)).isNotEmpty();
        assertThat(linksRepository.findById(tgChatId).get(0).uri().toString()).isEqualTo(link.toString());
    }

    @Test
    public void testRemoveLink() {
        Long tgChatId = 1L;
        URI link = URI.create("https://github.com/Rustut/LinkTracker");
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(link);

        when(gitHubExternalDataRepository.isProcessingUri(any())).thenReturn(true);
        when(gitHubExternalDataRepository.getLastUpdateDate(any())).thenReturn(OffsetDateTime.now());

        // Call the addLink method
        linksRepository.register(tgChatId);
        linkHandler.addLink(tgChatId, new AddLinkRequest(link, List.of(), List.of()));
        LinkResponse linkResponse = linkHandler.removeLink(tgChatId, removeLinkRequest);

        // Verify that the link was saved in the database
        assertThat(linksRepository.findById(tgChatId)).isEmpty();
    }
}
