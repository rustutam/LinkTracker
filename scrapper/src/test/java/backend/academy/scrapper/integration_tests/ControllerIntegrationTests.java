package backend.academy.scrapper.integration_tests;

import backend.academy.scrapper.handler.LinkHandler;
import backend.academy.scrapper.models.api.request.AddLinkRequest;
import backend.academy.scrapper.models.api.request.RemoveLinkRequest;
import backend.academy.scrapper.models.api.response.LinkResponse;
import backend.academy.scrapper.repository.database.LinksRepositoryImpl;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ControllerIntegrationTests {
    @Autowired
    private LinkHandler linkHandler;

    @Autowired
    private LinksRepositoryImpl linksRepository;

    @Test
    public void testAddLink() {
        Long tgChatId = 1L;
        URI link = URI.create("https://github.com/Rustut/LinkTracker");
        AddLinkRequest addLinkRequest = new AddLinkRequest(link, List.of(), List.of());

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

        // Call the addLink method
        linksRepository.register(tgChatId);
        linkHandler.addLink(tgChatId, new AddLinkRequest(link, List.of(), List.of()));
        LinkResponse linkResponse = linkHandler.removeLink(tgChatId, removeLinkRequest);

        // Verify that the link was saved in the database
        assertThat(linksRepository.findById(tgChatId)).isEmpty();
    }
}
