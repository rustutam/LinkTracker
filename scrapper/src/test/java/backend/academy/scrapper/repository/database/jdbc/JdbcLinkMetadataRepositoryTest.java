package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkMetadata;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.TagId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JdbcLinkMetadataRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcLinkMetadataRepository jdbcLinkMetadataRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllLinkMetadataByChatIdTest() {
        ChatId chatId = new ChatId(100L);

        URI linkUri1 = URI.create("https://github.com/java-rustutam/semester1");
        URI linkUri2 = URI.create("https://github.com/java-rustutam/semester2");
        URI linkUri3 = URI.create("https://github.com/java-rustutam/semester3");

        OffsetDateTime linkLastModifiedDate1 = OffsetDateTime.parse("2024-01-01 10:00:00", formatter.withZone(ZoneOffset.UTC));
        OffsetDateTime linkLastModifiedDate2 = OffsetDateTime.parse("2024-01-02 10:00:00", formatter.withZone(ZoneOffset.UTC));
        OffsetDateTime linkLastModifiedDate3 = OffsetDateTime.parse("2024-01-03 10:00:00", formatter.withZone(ZoneOffset.UTC));


        LinkMetadata linkMetadata1 = new LinkMetadata(
            Link.builder()
                .linkId(new LinkId(1L))
                .uri(linkUri1)
                .lastUpdateTime(linkLastModifiedDate1)
                .build(),
            List.of(
                new Tag(new TagId(1L), "tag1"),
                new Tag(new TagId(3L), "tag3")
            ),
            List.of(
                new Filter(new FilterId(1L), "filter1"),
                new Filter(new FilterId(3L), "filter3")
            )
        );
        LinkMetadata linkMetadata2 = new LinkMetadata(
            Link.builder()
                .linkId(new LinkId(2L))
                .uri(linkUri2)
                .lastUpdateTime(linkLastModifiedDate2)
                .build(),
            List.of(
                new Tag(new TagId(2L), "tag2")
            ),
            List.of(
                new Filter(new FilterId(2L), "filter2")
            )
        );
        LinkMetadata linkMetadata3 = new LinkMetadata(
            Link.builder()
                .linkId(new LinkId(3L))
                .uri(linkUri3)
                .lastUpdateTime(linkLastModifiedDate3)
                .build(),
            List.of(),
            List.of()
        );

        List<LinkMetadata> expectedLinkMetadata = List.of(
            linkMetadata1,
            linkMetadata2,
            linkMetadata3
        );

        List<LinkMetadata> allLinkMetadata = jdbcLinkMetadataRepository.findAllLinkMetadataByChatId(chatId);

        assertEquals(expectedLinkMetadata, allLinkMetadata);
    }

    @Test
    @Sql(scripts = "/sql/test_subscriptions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllLinkMetadataByChatIdWhenChatIdNotFoundTest() {
        ChatId chatId = new ChatId(123L);

        List<LinkMetadata> allLinkMetadata = jdbcLinkMetadataRepository.findAllLinkMetadataByChatId(chatId);

        assertTrue(allLinkMetadata.isEmpty());
    }
}
