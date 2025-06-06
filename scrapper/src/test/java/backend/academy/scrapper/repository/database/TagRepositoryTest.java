package backend.academy.scrapper.repository.database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.TestUtils;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.TagId;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

public abstract class TagRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private TagRepository tagRepository;

    private RecursiveComparisonConfiguration config;

    @BeforeEach
    void setUp() {
        config = TestUtils.CONFIG();
    }

    @Test
    @Sql(scripts = "/sql/insert_tags.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdTest() {
        TagId tagId = new TagId(1L);
        String expectedTagValue = "tag1";
        OffsetDateTime expectedCreatedAt = OffsetDateTime.parse("2024-10-01T21:35:03Z");

        Optional<Tag> tag = tagRepository.findById(tagId);

        assertTrue(tag.isPresent());
        assertEquals(tagId, tag.get().tagId());
        assertEquals(expectedTagValue, tag.get().value());
        assertEquals(expectedCreatedAt, tag.get().createdAt());
    }

    @Test
    @Sql(scripts = "/sql/insert_tags.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdWhenTagNotFoundTest() {
        TagId tagId = new TagId(100L);

        Optional<Tag> tag = tagRepository.findById(tagId);

        assertTrue(tag.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_tags.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByTagTest() {
        TagId expectedtagId = new TagId(1L);
        OffsetDateTime expectedCreatedAt = OffsetDateTime.parse("2024-10-01T21:35:03Z");
        String tagValue = "tag1";

        Optional<Tag> tag = tagRepository.findByTag(tagValue);

        assertTrue(tag.isPresent());
        assertEquals(expectedtagId, tag.get().tagId());
        assertEquals(tagValue, tag.get().value());
        assertEquals(expectedCreatedAt, tag.get().createdAt());
    }

    @Test
    @Sql(scripts = "/sql/insert_tags.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByTagWhenTagNotFoundTest() {
        String tagValue = "tag100";

        Optional<Tag> tag = tagRepository.findByTag(tagValue);

        assertTrue(tag.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_tags.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveTagSuccessfullyTest() {
        String tagValue = "tag100";

        Tag savedTag = tagRepository.save(tagValue);

        Optional<Tag> maybeTag = tagRepository.findByTag(tagValue);
        assertTrue(maybeTag.isPresent());
        assertThat(savedTag).usingRecursiveComparison(config).isEqualTo(maybeTag.get());
    }

    @Test
    @Sql(scripts = "/sql/insert_tags.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByTagIdSuccessfullyTest() {
        TagId tagId = new TagId(1L);

        tagRepository.deleteById(tagId);

        assertTrue(tagRepository.findById(tagId).isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_tags.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByChatIdSuccessfullyWhenDeleteMultipleUsersTest() {
        TagId tagId = new TagId(1L);
        TagId tagId2 = new TagId(2L);
        TagId tagId3 = new TagId(3L);
        TagId tagId4 = new TagId(4L);
        TagId tagId5 = new TagId(5L);

        tagRepository.deleteById(tagId);
        tagRepository.deleteById(tagId2);
        tagRepository.deleteById(tagId3);
        tagRepository.deleteById(tagId4);

        assertTrue(tagRepository.findById(tagId).isEmpty());
        assertTrue(tagRepository.findById(tagId2).isEmpty());
        assertTrue(tagRepository.findById(tagId3).isEmpty());
        assertTrue(tagRepository.findById(tagId4).isEmpty());
        assertTrue(tagRepository.findById(tagId5).isPresent());
    }
}
