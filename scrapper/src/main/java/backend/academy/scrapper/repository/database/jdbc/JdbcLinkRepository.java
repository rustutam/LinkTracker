package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.exceptions.NotExistLinkException;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.dto.LinkDto;
import backend.academy.scrapper.repository.database.LinkRepository;
import backend.academy.scrapper.repository.database.utilities.JdbcRowMapperUtil;
import backend.academy.scrapper.repository.database.utilities.mapper.LinkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Link> findAll() {
        List<LinkDto> linkEntities = jdbcTemplate.query(
            "SELECT * FROM scrapper.links",
            JdbcRowMapperUtil::mapRowToLink
        );

        return linkEntities.stream().map(LinkMapper::toDomain).toList();
    }

    @Override
    public Optional<Link> findById(LinkId linkId) {
        List<LinkDto> linkEntities = jdbcTemplate.query(
            "SELECT * FROM scrapper.links WHERE id = (?)",
            JdbcRowMapperUtil::mapRowToLink,
            linkId.id()
        );

        return linkEntities.stream().map(LinkMapper::toDomain).findFirst();
    }

    @Override
    public Optional<Link> findByUri(URI uri) {
        List<LinkDto> linkEntities = jdbcTemplate.query(
            "SELECT * FROM scrapper.links WHERE uri = (?)",
            JdbcRowMapperUtil::mapRowToLink,
            uri.toString()
        );

        return linkEntities.stream().map(LinkMapper::toDomain).findFirst();
    }

    @Override
    public Link updateLastModifying(LinkId linkId, OffsetDateTime newLastModifyingTime) {
        jdbcTemplate.update(
            "UPDATE scrapper.links SET last_modified_date = (?) WHERE id = (?)",
            newLastModifyingTime,
            linkId.id()
        );
        return findById(linkId).orElseThrow(NotExistLinkException::new);
    }

    @Override
    public Link save(URI uri) {
        LinkDto linkDto = jdbcTemplate.queryForObject(
            "INSERT INTO scrapper.links (uri) VALUES (?) RETURNING id, uri, last_modified_date, created_at",
            JdbcRowMapperUtil::mapRowToLink,
            uri.toString()
        );

        return LinkMapper.toDomain(linkDto);
    }


    @Override
    public Page<Link> findAll(Pageable pageable) {
        // 1. Вычисляем общее количество записей в таблице
        String countSql = "SELECT COUNT(*) FROM scrapper.links";
        int total = jdbcTemplate.queryForObject(countSql, Integer.class);

        // 2. Формируем запрос с пагинацией (LIMIT и OFFSET)
        List<LinkDto> linkEntities = jdbcTemplate.query(
            "SELECT * FROM scrapper.links ORDER BY id LIMIT (?) OFFSET (?)",
            JdbcRowMapperUtil::mapRowToLink,
            pageable.getPageSize(),
            pageable.getOffset()
        );

        List<Link> links = linkEntities.stream().map(LinkMapper::toDomain).toList();

        return new PageImpl<>(links, pageable, total);
    }
}
