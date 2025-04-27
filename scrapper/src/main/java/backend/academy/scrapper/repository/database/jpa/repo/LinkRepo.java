package backend.academy.scrapper.repository.database.jpa.repo;

import backend.academy.scrapper.models.entity.LinkEntity;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepo extends JpaRepository<LinkEntity, Long> {
    Optional<LinkEntity> findByUri(String uri);

    @NotNull Page<LinkEntity> findAll(@NotNull Pageable pageable);
}
