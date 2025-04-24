package backend.academy.scrapper.repository.database.jpa.repo;

import backend.academy.scrapper.models.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TagRepo extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByTag(String tag);

}
