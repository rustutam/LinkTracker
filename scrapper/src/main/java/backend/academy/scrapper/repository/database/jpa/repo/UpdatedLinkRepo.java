package backend.academy.scrapper.repository.database.jpa.repo;

import backend.academy.scrapper.models.entity.UpdatedLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdatedLinkRepo extends JpaRepository<UpdatedLinkEntity, Long> {
}
