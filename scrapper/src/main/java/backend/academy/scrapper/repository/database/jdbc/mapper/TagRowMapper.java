package backend.academy.scrapper.repository.database.jdbc.mapper;

import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.TagId;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TagRowMapper implements RowMapper<Tag> {
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TAG = "tag";
    private static final String COLUMN_CREATED_AT = "created_at";

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Tag(
                new TagId(rs.getLong(COLUMN_ID)),
                rs.getString(COLUMN_TAG),
                rs.getObject(COLUMN_CREATED_AT, OffsetDateTime.class));
    }
}
