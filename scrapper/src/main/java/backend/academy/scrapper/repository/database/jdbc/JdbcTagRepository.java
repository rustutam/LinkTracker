package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.models.entities.TagEntity;
import backend.academy.scrapper.repository.database.TagRepository;
import backend.academy.scrapper.repository.database.utilities.JdbcRowMapperUtil;
import java.util.List;
import java.util.Optional;
import backend.academy.scrapper.repository.database.utilities.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcTagRepository implements TagRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Tag> findById(TagId tagId) {
        List<TagEntity> tagEntities = jdbcTemplate.query(
            "SELECT * FROM scrapper.tags WHERE id = (?)",
            JdbcRowMapperUtil::mapRowToTag,
            tagId.id()
        );

        return tagEntities.stream().findFirst().map(TagMapper::toDomain);
    }

    @Override
    public Optional<Tag> findByTag(String tag) {
        List<TagEntity> tagEntities = jdbcTemplate.query(
            "SELECT * FROM scrapper.tags WHERE tag = (?)",
            JdbcRowMapperUtil::mapRowToTag,
            tag
        );

        return tagEntities.stream().findFirst().map(TagMapper::toDomain);
    }

    @Override
    public Tag save(String tag) {

        TagEntity tagEntity = jdbcTemplate.queryForObject(
            "INSERT INTO scrapper.tags (tag) VALUES (?) RETURNING id, tag, created_at",
            JdbcRowMapperUtil::mapRowToTag,
            tag
        );

        return TagMapper.toDomain(tagEntity);
    }

    @Override
    public void deleteById(TagId tagId) {
        jdbcTemplate.update(
            "DELETE FROM scrapper.tags WHERE id = (?)",
            tagId.id()
        );
    }
}
