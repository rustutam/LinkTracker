package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.exceptions.NotExistFilterException;
import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.repository.database.FilterRepository;
import backend.academy.scrapper.repository.database.jdbc.mapper.FilterRowMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcFilterRepository implements FilterRepository {
    private static final String SELECT_BY_ID =
        "SELECT id, filter, created_at FROM scrapper.filters WHERE id = ?";
    private static final String SELECT_BY_VALUE =
        "SELECT id, filter, created_at FROM scrapper.filters WHERE filter = ?";
    private static final String INSERT_SQL =
        "INSERT INTO scrapper.filters(filter) VALUES(?) RETURNING id, filter, created_at";
    private static final String DELETE_BY_ID_SQL =
        "DELETE FROM scrapper.filters WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final FilterRowMapper rowMapper;

    @Override
    public Optional<Filter> findById(FilterId filterId) {
        List<Filter> list = jdbcTemplate.query(
            SELECT_BY_ID,
            rowMapper,
            filterId.id()
        );
        return Optional.ofNullable(DataAccessUtils.singleResult(list));
    }

    @Override
    public Optional<Filter> findByFilter(String filter) {
        List<Filter> list = jdbcTemplate.query(
            SELECT_BY_VALUE,
            rowMapper,
            filter
        );
        return Optional.ofNullable(DataAccessUtils.singleResult(list));
    }

    @Override
    @Transactional
    public Filter save(String filter) {
        return jdbcTemplate.queryForObject(
            INSERT_SQL,
            rowMapper,
            filter
        );
    }

    @Override
    @Transactional
    public Filter deleteById(FilterId filterId) throws NotExistFilterException {
        Filter filter = findById(filterId).orElseThrow(NotExistFilterException::new);
        jdbcTemplate.update(
            DELETE_BY_ID_SQL,
            filterId.id()
        );
        return filter;
    }
}
