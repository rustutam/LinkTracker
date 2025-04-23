package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.exceptions.NotExistLinkException;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.ids.LinkId;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
class JdbcLinkRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllWithNonEmptyRepositoryTest() {
        List<Link> links = jdbcLinkRepository.findAll();
        assertEquals(6, links.size());
    }

    @Test
    void findAllWithEmptyRepositoryTest() {
        List<Link> links = jdbcLinkRepository.findAll();
        assertEquals(0, links.size());
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdTest() {
        String expectedUri = "https://github.com/java-rustutam/semester1";
        LinkId linkId = new LinkId(1L);
        Optional<Link> link = jdbcLinkRepository.findById(linkId);

        assertTrue(link.isPresent());
        assertEquals(linkId, link.get().linkId());
        assertEquals(expectedUri, link.get().uri().toString());
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdWhenLinkNotFoundTest() {
        LinkId linkId = new LinkId(100L);
        Optional<Link> link = jdbcLinkRepository.findById(linkId);

        assertTrue(link.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUriTest() {
        URI expectedUri = URI.create("https://github.com/java-rustutam/semester2");
        LinkId expectedLinkId = new LinkId(2L);
        Optional<Link> link = jdbcLinkRepository.findByUri(expectedUri);

        assertTrue(link.isPresent());
        assertEquals(expectedLinkId, link.get().linkId());
        assertEquals(expectedUri, link.get().uri());
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUriWhenLinkNotFoundTest() {
        URI expectedUri = URI.create("abracadabra");

        Optional<Link> link = jdbcLinkRepository.findByUri(expectedUri);

        assertTrue(link.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateLastModifyingTest() {
        LinkId linkId = new LinkId(1L);
        OffsetDateTime newLastModifyingTime = OffsetDateTime.MAX;

        Link updatedLink = jdbcLinkRepository.updateLastModifying(linkId, newLastModifyingTime);

        assertEquals(linkId, updatedLink.linkId());
        assertEquals(newLastModifyingTime, updatedLink.lastUpdateTime());
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateLastModifyingWhenLinkNotFoundTest() {
        LinkId linkId = new LinkId(100L);
        OffsetDateTime newLastModifyingTime = OffsetDateTime.MAX;

        assertThrows(NotExistLinkException.class, () ->
            jdbcLinkRepository.updateLastModifying(linkId, newLastModifyingTime)
        );
    }

    @Test
    void saveLinkSuccessfullyTest() {
        // Arrange
        URI uri = URI.create("https://github.com/java-rustutam/new-link");

        // Act
        Link savedLink = jdbcLinkRepository.save(uri);

        // Assert
        jdbcLinkRepository.findById(savedLink.linkId()).ifPresent(link -> assertEquals(savedLink, link));
        assertEquals(uri, savedLink.uri());
        assertNotNull(savedLink.linkId());
        assertNotNull(savedLink.lastUpdateTime());
    }

    @Test
    @Sql(scripts = "/sql/insert_links.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllWithPaginationTest() {
        // Arrange
        int pageSize = 2;
        Pageable firstPageable = PageRequest.of(0, pageSize);
        Pageable secondPageable = PageRequest.of(1, pageSize);
        Pageable thirdPageable = PageRequest.of(2, pageSize);

        List<URI> firstExpectedUris = List.of(
            URI.create("https://github.com/java-rustutam/semester1"),
            URI.create("https://github.com/java-rustutam/semester2")
        );

        List<URI> secondExpectedUris = List.of(
            URI.create("https://github.com/java-rustutam/semester3"),
            URI.create("https://github.com/java-rustutam/semester4")
        );

        List<URI> thirdExpectedUris = List.of(
            URI.create("https://github.com/java-rustutam/semester5"),
            URI.create("https://github.com/java-rustutam/semester6")
        );


        // Act
        Page<Link> firstPage = jdbcLinkRepository.findAll(firstPageable);
        Page<Link> secondPage = jdbcLinkRepository.findAll(secondPageable);
        Page<Link> thirdPage = jdbcLinkRepository.findAll(thirdPageable);

        // Assert
        assertEquals(firstExpectedUris, firstPage.getContent().stream().map(Link::uri).toList());
        assertEquals(secondExpectedUris, secondPage.getContent().stream().map(Link::uri).toList());
        assertEquals(thirdExpectedUris, thirdPage.getContent().stream().map(Link::uri).toList());
    }


}
