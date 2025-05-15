package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.models.domain.UpdatedLink;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.repository.database.LinkUpdateRepository;
import backend.academy.scrapper.repository.database.jdbc.mapper.UpdatedLinkRowMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcLinkUpdateRepository implements LinkUpdateRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UpdatedLinkRowMapper rowMapper;

    @Override
    public List<UpdatedLink> findAll() {
        String sql =
                """
                SELECT link_id, uri, description, chat_ids
                FROM scrapper.link_update
            """;

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public UpdatedLink save(UpdatedLink updatedLink) {
        String sql =
                """
                INSERT INTO scrapper.link_update (link_id, uri, description, chat_ids)
                VALUES (?, ?, ?, ?)
                RETURNING link_id, uri, description, chat_ids
            """;

        String chatIdsAsText = updatedLink.chatIds().stream()
                .map(chatId -> chatId.id().toString())
                .collect(Collectors.joining(","));

        return jdbcTemplate.queryForObject(
                sql,
                rowMapper,
                updatedLink.id().id(),
                updatedLink.uri().toString(),
                updatedLink.description(),
                chatIdsAsText);
    }

    @Override
    public UpdatedLink deleteById(LinkId id) {
        String sql =
                """
                DELETE FROM scrapper.link_update
                WHERE link_id = ?
                RETURNING link_id, uri, description, chat_ids
            """;

        return jdbcTemplate.queryForObject(sql, rowMapper, id.id());
    }
}
