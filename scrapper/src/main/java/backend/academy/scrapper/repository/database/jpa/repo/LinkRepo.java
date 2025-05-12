package backend.academy.scrapper.repository.database.jpa.repo;

import backend.academy.scrapper.models.entity.LinkEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LinkRepo extends JpaRepository<LinkEntity, Long> {
    Optional<LinkEntity> findByUri(String uri);

    @Query("SELECT l FROM LinkEntity l ORDER BY l.lastModifiedDate ASC")
    List<LinkEntity> findOldestLinks(Pageable pageable);
}
