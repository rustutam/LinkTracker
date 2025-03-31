package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.exceptions.NotExistTagException;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.models.entities.TagEntity;
import backend.academy.scrapper.repository.database.TagRepository;
import backend.academy.scrapper.repository.database.utilities.JdbcRowMapperUtil;
import java.util.List;
import java.util.Optional;
import backend.academy.scrapper.repository.database.utilities.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcTagRepository implements TagRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Tag> findById(TagId tagId) {
        List<TagEntity> tagEntities = jdbcTemplate.query(
            "SELECT * FROM tags WHERE id = (?)",
            JdbcRowMapperUtil::mapRowToTag,
            tagId.id()
        );

        return tagEntities.stream().findFirst().map(TagMapper::toDomain);
    }

    @Override
    public Optional<Tag> findByTag(String tag) {
        List<TagEntity> tagEntities = jdbcTemplate.query(
            "SELECT * FROM tags WHERE tag = (?)",
            JdbcRowMapperUtil::mapRowToTag,
            tag
        );

        return tagEntities.stream().findFirst().map(TagMapper::toDomain);
    }

    @Override
    public void save(String tag) {
        jdbcTemplate.update(
            "INSERT INTO tags (tag) VALUES (?)",
            tag
        );
    }

    @Override
    public void deleteById(TagId tagId) {
        jdbcTemplate.update(
            "DELETE FROM tags WHERE id = (?)",
            tagId.id()
        );
    }
}
