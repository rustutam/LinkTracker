package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.exceptions.NotExistFilterException;
import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.dto.FilterDto;
import backend.academy.scrapper.repository.database.FilterRepository;
import backend.academy.scrapper.repository.database.utilities.JdbcRowMapperUtil;
import backend.academy.scrapper.repository.database.jdbc.mapper.FilterRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcFilterRepository implements FilterRepository {
    private final JdbcTemplate jdbcTemplate;
    private final FilterRowMapper rowMapper;

    @Override
    public Optional<Filter> findById(FilterId filterId) {
        List<Filter> filters = jdbcTemplate.query(
            "SELECT * FROM scrapper.filters WHERE id = (?)",
            rowMapper,
            filterId.id()
        );

        return filters.stream().findFirst();
    }

    @Override
    public Optional<Filter> findByFilter(String filter) {
        List<Filter> filters = jdbcTemplate.query(
            "SELECT * FROM scrapper.filters WHERE filter = (?)",
            rowMapper,
            filter
        );

        return filters.stream().findFirst();
    }

    @Override
    public Filter save(String filter) {
        FilterDto filterDto = jdbcTemplate.queryForObject(
            "INSERT INTO scrapper.filters (filter) VALUES (?) RETURNING id, filter, created_at",
            JdbcRowMapperUtil::mapRowToFilter,
            filter
        );

        return FilterRowMapper.toDomain(filterDto);

    }

    @Override
    public Filter deleteById(FilterId filterId) throws NotExistFilterException {
        Filter filter = findById(filterId).orElseThrow(NotExistFilterException::new);
        jdbcTemplate.update(
            "DELETE FROM scrapper.filters WHERE id = (?)",
            filterId.id()
        );
        return filter;
    }
}
