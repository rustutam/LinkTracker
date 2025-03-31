package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.TagId;
import java.util.Optional;

public interface TagRepository {
    Optional<Tag> findById(TagId tagId);

    Optional<Tag> findByTag(String tag);

    void save(String tag);

    void deleteById(TagId tagId);
}
