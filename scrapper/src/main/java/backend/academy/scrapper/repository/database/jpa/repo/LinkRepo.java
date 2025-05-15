package backend.academy.scrapper.repository.database.jpa.repo;

import backend.academy.scrapper.models.entity.LinkEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepo extends JpaRepository<LinkEntity, Long> {
    Optional<LinkEntity> findByUri(String uri);
}
