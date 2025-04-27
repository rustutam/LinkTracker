package backend.academy.scrapper.repository.database.jpa;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.entity.SubscriptionEntity;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import backend.academy.scrapper.repository.database.jpa.mapper.SubscriptionMapper;
import backend.academy.scrapper.repository.database.jpa.repo.SubscriptionRepo;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaSubscriptionRepository implements SubscriptionRepository {
    private final SubscriptionRepo subscriptionRepo;
    private final SubscriptionMapper mapper;

    @Override
    public Subscription save(Subscription subscription) {
        SubscriptionEntity entity = mapper.toEntity(subscription);

        SubscriptionEntity savedEntity = subscriptionRepo.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Subscription remove(Subscription subscription) {
        SubscriptionEntity entity = mapper.toEntity(subscription);
        subscriptionRepo.delete(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Subscription> findById(SubscriptionId subscriptionId) {
        return subscriptionRepo.findById(subscriptionId.id())
            .map(mapper::toDomain);
    }

    @Override
    public Optional<Subscription> findByUserAndLink(User user, Link link) {
        long userId = user.userId().id();
        long linkId = link.linkId().id();
        return subscriptionRepo.findByUserIdAndLinkId(userId, linkId)
            .map(mapper::toDomain);
    }

    @Override
    public List<Subscription> findByUser(User user) {
        long userId = user.userId().id();
        return subscriptionRepo.findAllByUserId(userId)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public List<Subscription> findByLink(Link link) {
        long linkId = link.linkId().id();
        return subscriptionRepo.findAllByLinkId(linkId)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
}
