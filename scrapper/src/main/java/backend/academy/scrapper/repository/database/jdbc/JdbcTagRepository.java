package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.exceptions.NotExistTagException;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.repository.database.TagRepository;
import backend.academy.scrapper.repository.database.jdbc.mapper.TagRowMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcTagRepository implements TagRepository {
    private static final String BASE_COLUMNS = "id, tag, created_at";
    private static final String SELECT_BY_ID =
        "SELECT * FROM scrapper.tags WHERE id = ?";
    private static final String SELECT_BY_VALUE =
        "SELECT * FROM scrapper.tags WHERE tag = ?";
    private static final String INSERT_SQL =
        "INSERT INTO scrapper.tags (tag) VALUES (?) RETURNING " + BASE_COLUMNS;
    private static final String DELETE_BY_ID_SQL =
        "DELETE FROM scrapper.tags WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final TagRowMapper rowMapper;

    @Override
    public Optional<Tag> findById(TagId tagId) {
        List<Tag> list = jdbcTemplate.query(
            SELECT_BY_ID,
            rowMapper,
            tagId.id()
        );
        return Optional.ofNullable(DataAccessUtils.singleResult(list));
    }

    @Override
    public Optional<Tag> findByTag(String tag) {
        List<Tag> list = jdbcTemplate.query(
            SELECT_BY_VALUE,
            rowMapper,
            tag
        );
        return Optional.ofNullable(DataAccessUtils.singleResult(list));
    }

    @Override
    @Transactional
    public Tag save(String tag) {
        return jdbcTemplate.queryForObject(
            INSERT_SQL,
            rowMapper,
            tag
        );
    }

    @Override
    @Transactional
    public Tag deleteById(TagId tagId) {
        Tag tag = findById(tagId).orElseThrow(NotExistTagException::new);
        jdbcTemplate.update(
            DELETE_BY_ID_SQL,
            tagId.id()
        );
        return tag;
    }
}
