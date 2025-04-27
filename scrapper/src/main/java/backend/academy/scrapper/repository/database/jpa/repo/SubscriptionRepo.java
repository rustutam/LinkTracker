package backend.academy.scrapper.repository.database.jpa.repo;

import backend.academy.scrapper.models.entity.LinkEntity;
import backend.academy.scrapper.models.entity.SubscriptionEntity;
import backend.academy.scrapper.models.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepo extends JpaRepository<SubscriptionEntity, Long> {
    Optional<SubscriptionEntity> findByUserIdAndLinkId(Long userId, Long linkId);

    List<SubscriptionEntity> findAllByUserId(Long userId);

    List<SubscriptionEntity> findAllByLinkId(Long linkId);
}
