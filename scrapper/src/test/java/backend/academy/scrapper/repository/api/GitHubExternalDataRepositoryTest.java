package backend.academy.scrapper.repository.api;

import static backend.academy.scrapper.ClientsResponses.githubApiResponseJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.TestUtils;
import backend.academy.scrapper.client.GithubClient;
import backend.academy.scrapper.models.domain.ChangeInfo;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.ids.LinkId;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
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

    private RecursiveComparisonConfiguration config;

    private ChangeInfo expectedChangeInfo1 = new ChangeInfo("Новый PR/Issue", "New test issue 1", "rustutam", OffsetDateTime.parse("2025-04-29T08:40:47Z"),"New test body 1");
    private ChangeInfo expectedChangeInfo2 = new ChangeInfo("Новый PR/Issue", "New test issue 2", "rustutam", OffsetDateTime.parse("2025-04-28T15:58:24Z"),"New test body 2");
    private ChangeInfo expectedChangeInfo3 =new ChangeInfo("Новый PR/Issue", "New test issue 3", "rustutam", OffsetDateTime.parse("2025-04-01T17:56:11Z"),"New test body 3");

    @BeforeEach
    void setUp() {
        config = TestUtils.CONFIG();
    }

    @Test
    void whenCallGetChangeInfoByLinkThenReturnListWithLinkUpdates() {
        List<ChangeInfo> expectedList = List.of(
                expectedChangeInfo1,
                expectedChangeInfo2,
                expectedChangeInfo3
        );
        when(githubClient.issuesRequest("rustutam", "TestRepo"))
            .thenReturn(Optional.of(githubApiResponseJsonString));

        Link link = new Link(
                new LinkId(1L),
                URI.create("https://github.com/rustutam/TestRepo"),
                OffsetDateTime.parse("2023-10-10T12:00:00Z"),
                OffsetDateTime.parse("2023-10-10T12:00:00Z"));

        List<ChangeInfo> changeInfoByLink = gitHubExternalDataRepository.getChangeInfoByLink(link);

        assertNotNull(changeInfoByLink);
        assertThat(changeInfoByLink).usingRecursiveComparison(config).isEqualTo(expectedList);

    }
}
