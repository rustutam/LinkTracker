package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.TestUtils;
import backend.academy.scrapper.models.domain.UpdatedLink;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class LinkUpdateRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private LinkUpdateRepository linkUpdateRepository;

    private RecursiveComparisonConfiguration config;

    @BeforeEach
    void setUp() {
        config = TestUtils.CONFIG();
    }

    @Test
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveLinkUpdateTest() {
        UpdatedLink updatedLink = new UpdatedLink(
            new LinkId(123L),
            URI.create("https://example.com/updated"),
            "Обновление тест",
            List.of(new ChatId(11L), new ChatId(12L), new ChatId(13L))
        );

        linkUpdateRepository.save(updatedLink);

        UpdatedLink foundLink = linkUpdateRepository.findAll().getFirst();
        assertEquals(updatedLink.id(), foundLink.id());
        assertEquals(updatedLink.uri(), foundLink.uri());
        assertEquals(updatedLink.description(), foundLink.description());
        assertEquals(updatedLink.chatIds(), foundLink.chatIds());
    }

    @Test
    @Sql(scripts = "/sql/insert_updated_link.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllTest() {
        List<UpdatedLink> expectedUpdatedLinks = List.of(
            new UpdatedLink(
                new LinkId(1L),
                URI.create("https://example.com/page1"),
                "Первое обновление",
                List.of(new ChatId(123L), new ChatId(456L))
            ),
            new UpdatedLink(
                new LinkId(2L),
                URI.create("https://example.com/page2"),
                "Второе обновление",
                List.of(new ChatId(789L))
            ),
            new UpdatedLink(
                new LinkId(3L),
                URI.create("https://example.com/page3"),
                "Третье обновление",
                List.of(new ChatId(111L), new ChatId(222L), new ChatId(333L))
            ),
            new UpdatedLink(
                new LinkId(4L),
                URI.create("https://example.com/page4"),
                "Четвёртое обновление",
                List.of(new ChatId(444L))
            ),
            new UpdatedLink(
                new LinkId(5L),
                URI.create("https://example.com/page5"),
                "Пятое обновление",
                List.of(new ChatId(555L), new ChatId(666L))
            )
        );

        List<UpdatedLink> updatedLinks = linkUpdateRepository.findAll();

        assertThat(updatedLinks).usingRecursiveComparison(config).isEqualTo(expectedUpdatedLinks);
    }

    @Test
    @Sql(scripts = "/sql/insert_updated_link.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deletedByIdTest() {
        List<UpdatedLink> expectedUpdatedLinks = List.of(
            new UpdatedLink(
                new LinkId(1L),
                URI.create("https://example.com/page1"),
                "Первое обновление",
                List.of(new ChatId(123L), new ChatId(456L))
            ),
            new UpdatedLink(
                new LinkId(3L),
                URI.create("https://example.com/page3"),
                "Третье обновление",
                List.of(new ChatId(111L), new ChatId(222L), new ChatId(333L))
            ),
            new UpdatedLink(
                new LinkId(4L),
                URI.create("https://example.com/page4"),
                "Четвёртое обновление",
                List.of(new ChatId(444L))
            ),
            new UpdatedLink(
                new LinkId(5L),
                URI.create("https://example.com/page5"),
                "Пятое обновление",
                List.of(new ChatId(555L), new ChatId(666L))
            )
        );

        UpdatedLink expectedDeletedUpdatedLink = new UpdatedLink(
            new LinkId(2L),
            URI.create("https://example.com/page2"),
            "Второе обновление",
            List.of(new ChatId(789L))
        );

        UpdatedLink deletedUpdatedLink = linkUpdateRepository.deleteById(new LinkId(2L));

        assertThat(expectedDeletedUpdatedLink).usingRecursiveComparison(config).isEqualTo(deletedUpdatedLink);
        assertThat(linkUpdateRepository.findAll()).usingRecursiveComparison(config).isEqualTo(expectedUpdatedLinks);
    }
}
