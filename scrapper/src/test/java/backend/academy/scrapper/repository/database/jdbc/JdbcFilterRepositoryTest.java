package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.IntegrationEnvironment;
import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.ids.FilterId;
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
class JdbcFilterRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcFilterRepository jdbcFilterRepository;

    @Test
    @Sql(scripts = "/sql/insert_filters.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdTest() {
        FilterId filterId = new FilterId(1L);
        String expectedFilterValue = "filter1";

        Optional<Filter> filter = jdbcFilterRepository.findById(filterId);

        assertTrue(filter.isPresent());
        assertEquals(filterId, filter.get().filterId());
        assertEquals(expectedFilterValue, filter.get().value());
    }

    @Test
    @Sql(scripts = "/sql/insert_filters.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdWhenFilterNotFoundTest() {
        FilterId filterId = new FilterId(100L);

        Optional<Filter> filter = jdbcFilterRepository.findById(filterId);

        assertTrue(filter.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_filters.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByFilterTest() {
        FilterId expectedFilterId = new FilterId(1L);
        String filterValue = "filter1";

        Optional<Filter> filter = jdbcFilterRepository.findByFilter(filterValue);

        assertTrue(filter.isPresent());
        assertEquals(expectedFilterId, filter.get().filterId());
        assertEquals(filterValue, filter.get().value());
    }

    @Test
    @Sql(scripts = "/sql/insert_filters.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByFilterWhenFilterNotFoundTest() {
        String filterValue = "filter100";

        Optional<Filter> filter = jdbcFilterRepository.findByFilter(filterValue);

        assertTrue(filter.isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_filters.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveFilterSuccessfullyTest() {
        String filterValue = "filter100";

        jdbcFilterRepository.save(filterValue);

        assertTrue(jdbcFilterRepository.findByFilter(filterValue).isPresent());
    }

    @Test
    @Sql(scripts = "/sql/insert_filters.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByFilterIdSuccessfullyTest() {
        FilterId filterId = new FilterId(1L);

        jdbcFilterRepository.deleteById(filterId);

        assertTrue(jdbcFilterRepository.findById(filterId).isEmpty());
    }

    @Test
    @Sql(scripts = "/sql/insert_filters.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByChatIdSuccessfullyWhenDeleteMultipleUsersTest() {
        FilterId filterId = new FilterId(1L);
        FilterId filterId2 = new FilterId(2L);
        FilterId filterId3 = new FilterId(3L);
        FilterId filterId4 = new FilterId(4L);
        FilterId filterId5 = new FilterId(5L);

        jdbcFilterRepository.deleteById(filterId);
        jdbcFilterRepository.deleteById(filterId2);
        jdbcFilterRepository.deleteById(filterId3);
        jdbcFilterRepository.deleteById(filterId4);

        assertTrue(jdbcFilterRepository.findById(filterId).isEmpty());
        assertTrue(jdbcFilterRepository.findById(filterId2).isEmpty());
        assertTrue(jdbcFilterRepository.findById(filterId3).isEmpty());
        assertTrue(jdbcFilterRepository.findById(filterId4).isEmpty());
        assertTrue(jdbcFilterRepository.findById(filterId5).isPresent());


    }
}
