package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.exceptions.NotExistTagException;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.models.dto.TagDto;
import backend.academy.scrapper.repository.database.TagRepository;
import backend.academy.scrapper.repository.database.utilities.JdbcRowMapperUtil;
import backend.academy.scrapper.repository.database.jdbc.mapper.TagMapper;
import java.util.List;
import java.util.Optional;
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
        List<TagDto> tagEntities = jdbcTemplate.query(
            "SELECT * FROM scrapper.tags WHERE id = (?)",
            JdbcRowMapperUtil::mapRowToTag,
            tagId.id()
        );

        return tagEntities.stream().findFirst().map(TagMapper::toDomain);
    }

    @Override
    public Optional<Tag> findByTag(String tag) {
        List<TagDto> tagEntities = jdbcTemplate.query(
            "SELECT * FROM scrapper.tags WHERE tag = (?)",
            JdbcRowMapperUtil::mapRowToTag,
            tag
        );

        return tagEntities.stream().findFirst().map(TagMapper::toDomain);
    }

    @Override
    public Tag save(String tag) {

        TagDto tagDto = jdbcTemplate.queryForObject(
            "INSERT INTO scrapper.tags (tag) VALUES (?) RETURNING id, tag, created_at",
            JdbcRowMapperUtil::mapRowToTag,
            tag
        );

        return TagMapper.toDomain(tagDto);
    }

    @Override
    public Tag deleteById(TagId tagId) {
        Tag tag = findById(tagId).orElseThrow(NotExistTagException::new);
        //TODO добавить проверку есть ли такая запись и если нет  удалять
        jdbcTemplate.update(
            "DELETE FROM scrapper.tags WHERE id = (?)",
            tagId.id()
        );
        return tag;
    }
}
