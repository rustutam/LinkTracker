package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.dto.FilterDto;
import backend.academy.scrapper.repository.database.FilterRepository;
import backend.academy.scrapper.repository.database.utilities.JdbcRowMapperUtil;
import backend.academy.scrapper.repository.database.utilities.mapper.FilterMapper;
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

    @Override
    public Optional<Filter> findById(FilterId filterId) {
        List<FilterDto> filterEntities = jdbcTemplate.query(
            "SELECT * FROM scrapper.filters WHERE id = (?)",
            JdbcRowMapperUtil::mapRowToFilter,
            filterId.id()
        );

        return filterEntities.stream().map(FilterMapper::toDomain).findFirst();
    }

    @Override
    public Optional<Filter> findByFilter(String filter) {
        List<FilterDto> filterEntities = jdbcTemplate.query(
            "SELECT * FROM scrapper.filters WHERE filter = (?)",
            JdbcRowMapperUtil::mapRowToFilter,
            filter
        );

        return filterEntities.stream().map(FilterMapper::toDomain).findFirst();
    }

    @Override
    public Filter save(String filter) {
        FilterDto filterDto = jdbcTemplate.queryForObject(
            "INSERT INTO scrapper.filters (filter) VALUES (?) RETURNING id, filter, created_at",
            JdbcRowMapperUtil::mapRowToFilter,
            filter
        );

        return FilterMapper.toDomain(filterDto);

    }

    @Override
    public void deleteById(FilterId filterId) {
        jdbcTemplate.update(
            "DELETE FROM scrapper.filters WHERE id = (?)",
            filterId.id()
        );
    }
}
