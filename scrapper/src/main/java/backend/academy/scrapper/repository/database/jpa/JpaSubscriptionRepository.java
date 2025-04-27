package backend.academy.scrapper.repository.database.jpa;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.Subscription;
import backend.academy.scrapper.models.domain.User;
import backend.academy.scrapper.models.domain.ids.SubscriptionId;
import backend.academy.scrapper.models.entity.SubscriptionEntity;
import backend.academy.scrapper.models.entity.UserEntity;
import backend.academy.scrapper.repository.database.SubscriptionRepository;
import backend.academy.scrapper.repository.database.jpa.mapper.LinkMapper;
import backend.academy.scrapper.repository.database.jpa.mapper.SubscriptionMapper;
import backend.academy.scrapper.repository.database.jpa.mapper.UserMapper;
import backend.academy.scrapper.repository.database.jpa.repo.SubscriptionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaSubscriptionRepository implements SubscriptionRepository {
    private final SubscriptionRepo subscriptionRepo;

    @Override
    public Subscription save(Subscription subscription) {

        SubscriptionEntity entity = new SubscriptionEntity();
        entity.link(LinkMapper.map(subscription.link()));

        entity = subscriptionRepo.save(entity);
        return SubscriptionMapper.toDomain(entity);
    }

    @Override
    public Subscription remove(Subscription subscription) {
        return null;
    }

    @Override
    public Optional<Subscription> findById(SubscriptionId subscriptionId) {
        return subscriptionRepo.findById(subscriptionId.id())
                .map(SubscriptionMapper::toDomain);
    }

    @Override
    public Optional<Subscription> findByUserAndLink(User user, Link link) {
        long userId = user.userId().id();
        long linkId = link.linkId().id();
        return subscriptionRepo.findByUserIdAndLinkId(userId, linkId)
                .map(SubscriptionMapper::toDomain);
    }

    @Override
    public List<Subscription> findByUser(User user) {
        long userId = user.userId().id();
        return subscriptionRepo.findAllByUserId(userId)
                .stream()
                .map(SubscriptionMapper::toDomain)
                .toList();
    }

    @Override
    public List<Subscription> findByLink(Link link) {
        long linkId = link.linkId().id();
        return subscriptionRepo.findAllByLinkId(linkId)
                .stream()
                .map(SubscriptionMapper::toDomain)
                .toList();
    }
}
