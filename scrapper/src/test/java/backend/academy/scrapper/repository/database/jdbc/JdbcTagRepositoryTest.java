package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.TagId;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class JdbcTagRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcTagRepository jdbcTagRepository;

    @Test
    @Sql(scripts = "/sql/insert_tags.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdTest() {
        TagId tagId = new TagId(1L);
        String expectedTagValue = "tag1";

        Optional<Tag> tag = jdbcTagRepository.findById(tagId);

        assertTrue(tag.isPresent());
        assertEquals(tagId, tag.get().tagId());
        assertEquals(expectedTagValue, tag.get().value());
    }

    @Test
    @Sql(scripts = "/sql/insert_tags.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdWhenTagNotFoundTest() {
        TagId tagId = new TagId(100L);

        Optional<Tag> tag = jdbcTagRepository.findById(tagId);

        assertTrue(tag.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_tags.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByTagTest() {
        TagId expectedtagId = new TagId(1L);
        String tagValue = "tag1";

        Optional<Tag> tag = jdbcTagRepository.findByTag(tagValue);

        assertTrue(tag.isPresent());
        assertEquals(expectedtagId, tag.get().tagId());
        assertEquals(tagValue, tag.get().value());
    }

    @Test
    @Sql(scripts = "/sql/insert_tags.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByTagWhenTagNotFoundTest() {
        String tagValue = "tag100";

        Optional<Tag> tag = jdbcTagRepository.findByTag(tagValue);

        assertTrue(tag.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_tags.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveTagSuccessfullyTest() {
        String tagValue = "tag100";

        jdbcTagRepository.save(tagValue);

        assertTrue(jdbcTagRepository.findByTag(tagValue).isPresent());
    }

    @Test
    @Sql(scripts = "/sql/insert_tags.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByTagIdSuccessfullyTest() {
        TagId tagId = new TagId(1L);

        jdbcTagRepository.deleteById(tagId);

        assertTrue(jdbcTagRepository.findById(tagId).isEmpty());
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

        jdbcTagRepository.deleteById(tagId);
        jdbcTagRepository.deleteById(tagId2);
        jdbcTagRepository.deleteById(tagId3);
        jdbcTagRepository.deleteById(tagId4);

        assertTrue(jdbcTagRepository.findById(tagId).isEmpty());
        assertTrue(jdbcTagRepository.findById(tagId2).isEmpty());
        assertTrue(jdbcTagRepository.findById(tagId3).isEmpty());
        assertTrue(jdbcTagRepository.findById(tagId4).isEmpty());
        assertTrue(jdbcTagRepository.findById(tagId5).isPresent());


    }
}

