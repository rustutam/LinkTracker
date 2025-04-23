package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkMetadata;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.repository.database.LinkMetadataRepository;
import backend.academy.scrapper.repository.database.LinkRepository;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "SQL")
public class JdbcLinkMetadataRepository implements LinkMetadataRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SubscriptionRepository subscriptionRepository;
    private final LinkRepository linkRepository;

    @Override
    @Transactional
    public List<LinkMetadata> findAllLinkMetadataByChatId(ChatId chatId) {
        String sql = """
            SELECT s.id AS subscription_id, l.id AS link_id
            FROM scrapper.subscriptions s
            JOIN scrapper.users u ON s.user_id = u.id
            JOIN scrapper.links l ON s.link_id = l.id
            WHERE u.chat_id = (?)
            """;

        return jdbcTemplate.query(
            sql,
            ps -> ps.setLong(1, chatId.id()),
            rs -> {
                List<LinkMetadata> result = new ArrayList<>();

                while (rs.next()) {
                    SubscriptionId subscriptionId = new SubscriptionId(rs.getLong("subscription_id"));
                    LinkId linkId = new LinkId(rs.getLong("link_id"));

                    Link link = linkRepository.findById(linkId).orElseThrow();

                    // Получаем метаинформацию
                    List<Tag> tags = subscriptionRepository.findTagsBySubscriptionId(subscriptionId);
                    List<Filter> filters = subscriptionRepository.findFiltersBySubscriptionId(subscriptionId);

                    result.add(new LinkMetadata(link, tags, filters));
                }

                return result;
            }
        );
    }
}
