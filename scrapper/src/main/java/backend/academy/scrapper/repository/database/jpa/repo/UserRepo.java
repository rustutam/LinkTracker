package backend.academy.scrapper.repository.database.jpa.repo;

import backend.academy.scrapper.models.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
}
