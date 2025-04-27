package backend.academy.scrapper.repository.database.jpa.repo;

import backend.academy.scrapper.models.entity.TagEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepo extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByTag(String tag);

}
