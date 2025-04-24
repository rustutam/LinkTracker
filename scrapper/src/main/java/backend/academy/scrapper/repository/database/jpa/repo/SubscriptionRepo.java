package backend.academy.scrapper.repository.database.jpa.repo;

import backend.academy.scrapper.models.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepo extends JpaRepository<SubscriptionEntity, Long> {
}
