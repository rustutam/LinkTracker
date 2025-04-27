package backend.academy.scrapper.repository.database.jdbc.mapper;

import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.dto.FilterDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

@Component
public class FilterRowMapper implements RowMapper<Filter> {
    public static Filter toDomain(FilterDto entity) {
        return new Filter(
            new FilterId(entity.id()),
            entity.value()
        );
    }

    public static List<Filter> toDomain(List<FilterDto> filterEntities) {
        return filterEntities.stream()
            .map(FilterRowMapper::toDomain)
            .toList();
    }

    @Override
    public Filter mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Filter(
            new FilterId(rs.getLong("id")),
            rs.getString("filter")
        );
    }
}
