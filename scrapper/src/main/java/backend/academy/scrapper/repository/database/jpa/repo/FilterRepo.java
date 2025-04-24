package backend.academy.scrapper.repository.database.jpa.repo;

import backend.academy.scrapper.models.entity.FilterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FilterRepo extends JpaRepository<FilterEntity, Long> {
    Optional<FilterEntity> findByFilter(String filter);
}
