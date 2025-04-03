package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkMetadata;
import backend.academy.scrapper.models.domain.ids.ChatId;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.entities.LinkMetadataEntity;
import backend.academy.scrapper.repository.database.FilterRepository;
import backend.academy.scrapper.repository.database.LinkMetadataRepository;
import backend.academy.scrapper.repository.database.TagRepository;
import backend.academy.scrapper.repository.database.utilities.JdbcRowMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.net.URI;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcLinkMetadataRepository implements LinkMetadataRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TagRepository tagRepository;
    private final FilterRepository filterRepository;

    @Override
    public List<LinkMetadata> findAllLinkMetadataByChatId(ChatId chatId) {
        String sql = """
            SELECT s.id AS subscription_id, l.id AS link_id, l.uri, l.last_modified_date AS link_last_modified_date,
            FROM subscriptions s
            JOIN users u ON s.user_id = u.id
            JOIN links l ON s.link_id = l.id
            WHERE u.chat_id = (?)
            """;

        List<LinkMetadataEntity> linkMetadataEntities = jdbcTemplate.query(
            sql,
            JdbcRowMapperUtil::mapRowToLinkMetadataEntity,
            chatId.id()
        );


        return linkMetadataEntities.stream()
            .map(
                linkMetadataEntity -> {

                    Link link = Link.builder()
                        .linkId(new LinkId(linkMetadataEntity.linkId()))
                        .uri(URI.create(linkMetadataEntity.linkUri()))
                        .lastUpdateTime(linkMetadataEntity.linkLastModifiedDate())
                        .build();

                    SubscriptionId subscriptionId = new SubscriptionId(linkMetadataEntity.subscriptionId());

                    return LinkMetadata.builder()
                        .link(link)
                        .tags(tagRepository.findBySubscriptionId(subscriptionId))
                        .filters(filterRepository.findBySubscriptionId(subscriptionId))
                        .build();

                }
            )
            .toList();
    }
}
