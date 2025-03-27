package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.exceptions.AlreadyTrackLinkException;
import backend.academy.scrapper.exceptions.NotExistLinkException;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.entities.LinkEntity;
import backend.academy.scrapper.repository.database.LinkRepository;
import backend.academy.scrapper.repository.database.utilities.JdbcRowMapperUtil;
import backend.academy.scrapper.repository.database.utilities.mapper.LinkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Link> findAll() {
        List<LinkEntity> linkEntities = jdbcTemplate.query(
                "SELECT * FROM links",
                JdbcRowMapperUtil::mapRowToLink
        );

        return linkEntities.stream().map(LinkMapper::toDomain).toList();
    }

    @Override
    public Optional<Link> findById(LinkId linkId) {
        List<LinkEntity> linkEntities = jdbcTemplate.query(
                "SELECT * FROM links WHERE id = (?)",
                JdbcRowMapperUtil::mapRowToLink,
                linkId.id()
        );

        return linkEntities.stream().map(LinkMapper::toDomain).findFirst();
    }

    @Override
    public Optional<Link> findByUri(URI uri) {
        List<LinkEntity> linkEntities = jdbcTemplate.query(
                "SELECT * FROM links WHERE uri = (?)",
                JdbcRowMapperUtil::mapRowToLink,
                uri.toString()
        );

        return linkEntities.stream().map(LinkMapper::toDomain).findFirst();
    }

    @Override
    public Link updateLastModifying(LinkId linkId, OffsetDateTime newLastModifyingTime) {
        jdbcTemplate.update(
                "UPDATE links SET last_modified_date = (?) WHERE id = (?)",
                newLastModifyingTime,
                linkId.id()
        );
        return findById(linkId).orElseThrow(NotExistLinkException::new);
    }

    @Override
    public Link save(URI uri) {
        try {
            jdbcTemplate.update(
                "INSERT INTO links (uri) VALUES (?)",
                uri
            );
        } catch (DuplicateKeyException e) {
            throw new AlreadyTrackLinkException();
        }

        return findByUri(uri).orElseThrow(NotExistLinkException::new);
    }
}
