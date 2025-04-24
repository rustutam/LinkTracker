package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.domain.ids.TagId;
import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Optional<Tag> findById(TagId tagId);

    Optional<Tag> findByTag(String tag);

    Tag save(String tag);

    Tag deleteById(TagId tagId);
}
