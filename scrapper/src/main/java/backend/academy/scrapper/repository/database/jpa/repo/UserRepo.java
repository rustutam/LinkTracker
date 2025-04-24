package backend.academy.scrapper.repository.database.jpa.repo;

import backend.academy.scrapper.models.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
    boolean existsByChatId(Long chatId);

    Optional<UserEntity> findByChatId(Long chatId);
}
