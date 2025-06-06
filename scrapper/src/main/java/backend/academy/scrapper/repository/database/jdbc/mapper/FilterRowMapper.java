package backend.academy.scrapper.repository.database.jdbc.mapper;

import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.ids.FilterId;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class FilterRowMapper implements RowMapper<Filter> {
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FILTER = "filter";
    private static final String COLUMN_CREATED_AT = "created_at";

    @Override
    public Filter mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Filter(
                new FilterId(rs.getLong(COLUMN_ID)),
                rs.getString(COLUMN_FILTER),
                rs.getObject(COLUMN_CREATED_AT, OffsetDateTime.class));
    }
}
