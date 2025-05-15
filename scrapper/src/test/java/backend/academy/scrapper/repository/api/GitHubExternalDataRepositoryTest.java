package backend.academy.scrapper.repository.api;

import static backend.academy.scrapper.ClientsResponses.githubApiResponseJsonString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.client.GithubClient;
import backend.academy.scrapper.models.domain.ChangeInfo;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.ids.LinkId;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class GitHubExternalDataRepositoryTest extends IntegrationEnvironment {

    @MockitoBean
    private GithubClient githubClient;

    @Autowired
    private GitHubExternalDataRepository gitHubExternalDataRepository;

    @Test
    void whenCallGetChangeInfoByLinkThenReturnListWithLinkUpdates() {

        when(githubClient.issuesRequest("rustutam", "TestRepo")).thenReturn(githubApiResponseJsonString);

        Link link = new Link(
                new LinkId(1L),
                URI.create("https://github.com/rustutam/TestRepo"),
                OffsetDateTime.parse("2023-10-10T12:00:00Z"),
                OffsetDateTime.parse("2023-10-10T12:00:00Z"));

        List<ChangeInfo> changeInfoByLink = gitHubExternalDataRepository.getChangeInfoByLink(link);

        assertNotNull(changeInfoByLink);
    }
}
