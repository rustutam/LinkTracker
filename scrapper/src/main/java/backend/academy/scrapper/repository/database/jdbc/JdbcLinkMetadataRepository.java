package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.models.domain.LinkMetadata;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.repository.database.LinkMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcLinkMetadataRepository implements LinkMetadataRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<LinkMetadata> findAllLinksByChatId(ChatId chatId) {
        String sql = """
            SELECT u.id AS user_id, u.chat_id, u.created_at AS user_created_at,
                   l.id AS link_id, l.uri, l.created_at AS link_created_at,
                   t.name AS tag_name, f.rule AS filter_rule, s.subscribed_at
            FROM subscriptions s
            INNER JOIN users u ON s.user_id = u.id
            INNER JOIN links l ON s.link_id = l.id
            LEFT JOIN tags t ON l.tag_id = t.id
            LEFT JOIN filters f ON l.filter_id = f.id
            ORDER BY u.id, l.id;
            """;
        jdbcTemplate.query(

        );
    }
}
