package backend.academy.scrapper.repository.database.jdbc.mapper;

import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.UserId;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class UserRowMapper implements RowMapper<User> {
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CHAT_ID = "chat_id";
    private static final String COLUMN_CREATED_AT = "created_at";

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
            new UserId(rs.getLong(COLUMN_ID)),
            new ChatId(rs.getLong(COLUMN_CHAT_ID)),
            rs.getObject(COLUMN_CREATED_AT, OffsetDateTime.class)
        );
    }
}
