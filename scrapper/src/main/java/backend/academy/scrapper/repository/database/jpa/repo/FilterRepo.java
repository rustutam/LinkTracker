package backend.academy.scrapper.repository.database.jpa.repo;

import backend.academy.scrapper.models.entity.FilterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRepo extends JpaRepository<FilterEntity, Long> {
}
