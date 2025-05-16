package backend.academy.scrapper.repository.database.jdbc.mapper;

import backend.academy.scrapper.models.domain.UpdatedLink;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class UpdatedLinkRowMapper implements RowMapper<UpdatedLink> {
    private static final String COLUMN_LINK_ID = "link_id";
    private static final String COLUMN_CHAT_ID = "chat_ids";
    private static final String COLUMN_URI = "uri";
    private static final String COLUMN_DESCRIPTION = "description";

    @Override
    public UpdatedLink mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<ChatId> chatIds = Arrays.stream(rs.getString(COLUMN_CHAT_ID).split(","))
                .map(String::trim) // убираем пробелы, если есть
                .map(s -> new ChatId(Long.parseLong(s))) // парсим в ChatId
                .toList();

        return new UpdatedLink(
                new LinkId(rs.getLong(COLUMN_LINK_ID)),
                URI.create(rs.getString(COLUMN_URI)),
                rs.getString(COLUMN_DESCRIPTION),
                chatIds);
    }
}
