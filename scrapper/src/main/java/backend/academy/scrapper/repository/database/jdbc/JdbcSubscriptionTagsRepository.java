package backend.academy.scrapper.repository.database.jdbc;

import backend.academy.scrapper.exceptions.NotExistTagException;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import backend.academy.scrapper.repository.database.SubscriptionTagsRepository;
import backend.academy.scrapper.repository.database.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcSubscriptionTagsRepository implements SubscriptionTagsRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SubscriptionRepository subscriptionRepository;
    private final TagRepository tagRepository;

    @Override
    public void save(SubscriptionId subscriptionId, TagId tagId) {
        subscriptionRepository;
        Tag tag = tagRepository.findById(tagId).orElseThrow(NotExistTagException::new);



        jdbcTemplate.update(
            "INSERT INTO subscription_tags (subscription_id, tag_id) VALUES (?, ?)",
            subscriptionId.id(),
            tagId.id()
        );

    }

    @Override
    public void delete(SubscriptionId subscriptionId, TagId tagId) {
        jdbcTemplate.update(
            "DELETE FROM subscription_tags WHERE subscription_id =? AND tag_id =?",
            subscriptionId.id(),
            tagId.id()
        );
    }
}
