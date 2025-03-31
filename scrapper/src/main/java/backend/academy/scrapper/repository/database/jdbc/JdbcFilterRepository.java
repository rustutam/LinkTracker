package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.entities.FilterEntity;
import backend.academy.scrapper.repository.database.FilterRepository;
import backend.academy.scrapper.repository.database.utilities.JdbcRowMapperUtil;
import backend.academy.scrapper.repository.database.utilities.mapper.FilterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import backend.academy.scrapper.exceptions.NotExistFilterException;

@Repository
@RequiredArgsConstructor
public class JdbcFilterRepository implements FilterRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Filter> findById(FilterId filterId) {
        List<FilterEntity> filterEntities = jdbcTemplate.query(
            "SELECT * FROM filters WHERE id = (?)",
            JdbcRowMapperUtil::mapRowToFilter,
            filterId.id()
        );

        return filterEntities.stream().map(FilterMapper::toDomain).findFirst();
    }

    @Override
    public Optional<Filter> findByFilter(String filter){
        List<FilterEntity> filterEntities = jdbcTemplate.query(
            "SELECT * FROM filters WHERE filter = (?)",
            JdbcRowMapperUtil::mapRowToFilter,
            filter
        );

        return filterEntities.stream().map(FilterMapper::toDomain).findFirst();
    }

    @Override
    public void save(String filter) {
        jdbcTemplate.update(
            "INSERT INTO filters (filter) VALUES (?)",
            filter
        );
    }

    @Override
    public void deleteById(FilterId filterId) {
        jdbcTemplate.update(
            "DELETE FROM filters WHERE id = (?)",
            filterId.id()
        );
    }
}
