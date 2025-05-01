package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.TestUtils;
import backend.academy.scrapper.exceptions.NotExistLinkException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class LinkRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private LinkRepository linkRepository;

    private RecursiveComparisonConfiguration config;

    @BeforeEach
    void setUp() {
        config = TestUtils.CONFIG();
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllPaginatedWithNonEmptyRepositoryTest() {
        List<Link> links = linkRepository.findAll();
        assertEquals(6, links.size());
    }

    @Test
    void findAllPaginatedWithEmptyRepositoryTest() {
        List<Link> links = linkRepository.findAll();
        assertEquals(0, links.size());
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdTest() {
        LinkId linkId = new LinkId(1L);
        String expectedUri = "https://github.com/java-rustutam/semester1";
        OffsetDateTime expectedLastUpdateTime = OffsetDateTime.parse("2024-10-01T21:35:03Z");
        OffsetDateTime expectedCreatedAt = OffsetDateTime.parse("2024-10-01T21:35:03Z");

        Optional<Link> link = linkRepository.findById(linkId);

        assertTrue(link.isPresent());
        assertEquals(linkId, link.get().linkId());
        assertEquals(expectedUri, link.get().uri().toString());
        assertEquals(expectedLastUpdateTime, link.get().lastUpdateTime());
        assertEquals(expectedCreatedAt, link.get().createdAt());
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdWhenLinkNotFoundTest() {
        LinkId linkId = new LinkId(100L);
        Optional<Link> link = linkRepository.findById(linkId);

        assertTrue(link.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUriTest() {
        URI expectedUri = URI.create("https://github.com/java-rustutam/semester2");
        LinkId expectedLinkId = new LinkId(2L);
        OffsetDateTime expectedLastUpdateTime = OffsetDateTime.parse("2024-10-02T21:35:03Z");
        OffsetDateTime expectedCreatedAt = OffsetDateTime.parse("2024-10-02T21:35:03Z");

        Optional<Link> link = linkRepository.findByUri(expectedUri);

        assertTrue(link.isPresent());
        assertEquals(expectedLinkId, link.get().linkId());
        assertEquals(expectedUri, link.get().uri());
        assertEquals(expectedLastUpdateTime, link.get().lastUpdateTime());
        assertEquals(expectedCreatedAt, link.get().createdAt());
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUriWhenLinkNotFoundTest() {
        URI expectedUri = URI.create("abracadabra");

        Optional<Link> link = linkRepository.findByUri(expectedUri);

        assertTrue(link.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateLastUpdateTimeTest() {
        LinkId linkId = new LinkId(1L);
        OffsetDateTime newLastModifyingTime = OffsetDateTime.parse("2025-10-01T21:35:03Z");
        OffsetDateTime expectedCreatedAt = OffsetDateTime.parse("2024-10-01T21:35:03Z");

        Link updatedLink = linkRepository.updateLastUpdateTime(linkId, newLastModifyingTime);

        assertEquals(linkId, updatedLink.linkId());
        assertEquals(newLastModifyingTime, updatedLink.lastUpdateTime());
        assertEquals(expectedCreatedAt, updatedLink.createdAt());
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateLastUpdateTimeWhenLinkNotFoundTest() {
        LinkId linkId = new LinkId(100L);
        OffsetDateTime newLastModifyingTime = OffsetDateTime.MIN;

        assertThrows(NotExistLinkException.class, () ->
            linkRepository.updateLastUpdateTime(linkId, newLastModifyingTime)
        );
    }

    @Test
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveLinkSuccessfullyTest() {
        URI uri = URI.create("https://github.com/java-rustutam/new-link");

        Link savedLink = linkRepository.save(uri);

        Optional<Link> maybeLink = linkRepository.findById(savedLink.linkId());
        assertTrue(maybeLink.isPresent());
        assertThat(savedLink)
            .usingRecursiveComparison(
                config
            )
            .isEqualTo(maybeLink.get());
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllPaginatedWithPaginationTest() {
        // Arrange
        URI uri1 = URI.create("https://github.com/java-rustutam/semester1");
        URI uri2 = URI.create("https://github.com/java-rustutam/semester2");
        URI uri3 = URI.create("https://github.com/java-rustutam/semester3");
        URI uri4 = URI.create("https://github.com/java-rustutam/semester4");
        URI uri5 = URI.create("https://github.com/java-rustutam/semester5");
        URI uri6 = URI.create("https://github.com/java-rustutam/semester6");

        Link link1 = linkRepository.findByUri(uri1).orElseThrow();
        Link link2 = linkRepository.findByUri(uri2).orElseThrow();
        Link link3 = linkRepository.findByUri(uri3).orElseThrow();
        Link link4 = linkRepository.findByUri(uri4).orElseThrow();
        Link link5 = linkRepository.findByUri(uri5).orElseThrow();
        Link link6 = linkRepository.findByUri(uri6).orElseThrow();

        int pageSize = 2;
        Pageable firstPageable = PageRequest.of(0, pageSize);
        Pageable secondPageable = PageRequest.of(1, pageSize);
        Pageable thirdPageable = PageRequest.of(2, pageSize);

        // Act
        Page<Link> firstPage = linkRepository.findAllPaginated(firstPageable);
        Page<Link> secondPage = linkRepository.findAllPaginated(secondPageable);
        Page<Link> thirdPage = linkRepository.findAllPaginated(thirdPageable);

        // Assert
        assertThat(firstPage.getContent())
            .usingRecursiveComparison(config)
            .isEqualTo(List.of(link1, link2));

        assertThat(secondPage.getContent())
            .usingRecursiveComparison(config)
            .isEqualTo(List.of(link3, link4));

        assertThat(thirdPage.getContent())
            .usingRecursiveComparison(config)
            .isEqualTo(List.of(link5, link6));
    }


}
