package backend.academy.scrapper.repository.database.jdbc.mapper;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.ids.LinkId;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LinkRowMapper implements RowMapper<Link> {
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_URI = "uri";
    private static final String COLUMN_LAST_MODIFIED_DATE = "last_modified_date";
    private static final String COLUMN_CREATED_AT = "created_at";

    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Link(
                new LinkId(rs.getLong(COLUMN_ID)),
                URI.create(rs.getString(COLUMN_URI)),
                rs.getObject(COLUMN_LAST_MODIFIED_DATE, OffsetDateTime.class),
                rs.getObject(COLUMN_CREATED_AT, OffsetDateTime.class));
    }
}
