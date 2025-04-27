package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.exceptions.NotExistLinkException;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.repository.database.LinkRepository;
import backend.academy.scrapper.repository.database.jdbc.mapper.LinkRowMapper;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcLinkRepository implements LinkRepository {
    private static final String BASE_COLUMNS = "id, uri, last_modified_date, created_at";

    private static final String SELECT_ALL =
        "SELECT * FROM scrapper.links";

    private static final String SELECT_BY_ID =
        "SELECT * FROM scrapper.links WHERE id = ?";

    private static final String SELECT_BY_URI =
        "SELECT * FROM scrapper.links WHERE uri = ?";

    private static final String INSERT_SQL =
        "INSERT INTO scrapper.links (uri) VALUES (?) RETURNING " + BASE_COLUMNS;

    private static final String UPDATE_SQL =
        "UPDATE scrapper.links SET last_modified_date = ? WHERE id = ? RETURNING " + BASE_COLUMNS;

    private static final String COUNT_SQL =
        "SELECT COUNT(*) FROM scrapper.links";

    private static final String SELECT_PAGINATED =
        "SELECT * FROM scrapper.links ORDER BY id LIMIT ? OFFSET ?";

    private final JdbcTemplate jdbcTemplate;
    private final LinkRowMapper rowMapper;

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query(SELECT_ALL, rowMapper);
    }

    @Override
    public Optional<Link> findById(LinkId linkId) {
        List<Link> list = jdbcTemplate.query(SELECT_BY_ID, rowMapper, linkId.id());
        return Optional.ofNullable(DataAccessUtils.singleResult(list));
    }

    @Override
    public Optional<Link> findByUri(URI uri) {
        List<Link> list = jdbcTemplate.query(SELECT_BY_URI, rowMapper, uri.toString());
        return Optional.ofNullable(DataAccessUtils.singleResult(list));
    }

    @Override
    @Transactional
    public Link updateLastUpdateTime(LinkId linkId, OffsetDateTime newLastModifyingTime) throws NotExistLinkException {
        try {
            return jdbcTemplate.queryForObject(UPDATE_SQL, rowMapper, newLastModifyingTime, linkId.id());
        } catch (EmptyResultDataAccessException ex) {
            log.atError()
                .addKeyValue("linkId", linkId.id())
                .addKeyValue("access-type", "SQL")
                .setMessage("Не существует ссылка с id = " + linkId + " в базе данных")
                .log();
            throw new NotExistLinkException();
        }
    }

    @Override
    @Transactional
    public Link save(URI uri) {
        return jdbcTemplate.queryForObject(INSERT_SQL, rowMapper, uri.toString());
    }


    @Override
    public Page<Link> findAllPaginated(Pageable pageable) {
        int total = jdbcTemplate.queryForObject(COUNT_SQL, Integer.class);
        List<Link> content = jdbcTemplate.query(
            SELECT_PAGINATED,
            rowMapper,
            pageable.getPageSize(),
            pageable.getOffset()
        );
        return new PageImpl<>(content, pageable, total);
    }
}
