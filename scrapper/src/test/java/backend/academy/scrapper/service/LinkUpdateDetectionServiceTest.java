package backend.academy.scrapper.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import backend.academy.scrapper.ClientsResponses;
import backend.academy.scrapper.TestModelFactory;
import backend.academy.scrapper.TestUtils;
import backend.academy.scrapper.client.GithubClient;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.UpdatedLink;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class LinkUpdateDetectionServiceTest {

    @Autowired
    LinkUpdateDetectionService linkUpdateDetectionService;

    @MockitoBean
    GithubClient githubClient;

    @MockitoBean
    SubscriptionRepository subscriptionRepository;

    private RecursiveComparisonConfiguration config;

    @BeforeEach
    void setUp() {
        config = TestUtils.CONFIG();
    }

    @Test
    void getUpdatedLinksTest() {
        List<Link> links = List.of(new Link(
                new LinkId(1L),
                URI.create("https://github.com/rustutam/TestRepo"),
                OffsetDateTime.parse("2024-10-01T21:35:03Z"),
                OffsetDateTime.parse("2024-10-01T21:35:03Z")));

        List<Subscription> subscriptions =
                List.of(TestModelFactory.createSubscription(), TestModelFactory.createSubscription());

        List<ChatId> expectedChatIds =
                subscriptions.stream().map(s -> s.user().chatId()).toList();
        List<UpdatedLink> expectedUpdatedLinks = List.of(
                new UpdatedLink(
                        new LinkId(1L),
                        URI.create("https://github.com/rustutam/TestRepo"),
                        "Новый PR/Issue. 2.\n" + "Автор: rustutam. Дата: 2025-05-15T14:00:03Z.\n" + " Описание: 2",
                        expectedChatIds),
                new UpdatedLink(
                        new LinkId(1L),
                        URI.create("https://github.com/rustutam/TestRepo"),
                        "Новый PR/Issue. 11.\n" + "Автор: rustutam. Дата: 2025-05-15T14:00Z.\n" + " Описание: 11",
                        expectedChatIds));

        when(githubClient.issuesRequest(any(), any())).thenReturn(ClientsResponses.githubApiResponseWithTwoIssues);
        when(subscriptionRepository.findByLink(any())).thenReturn(subscriptions);

        List<UpdatedLink> updatedLinks = linkUpdateDetectionService.getUpdatedLinks(links);

        assertThat(updatedLinks).usingRecursiveComparison(config).isEqualTo(expectedUpdatedLinks);
    }
}
