package backend.academy.scrapper.repository.database.jpa;

import backend.academy.scrapper.models.domain.UpdatedLink;
import backend.academy.scrapper.models.domain.ids.LinkId;
import backend.academy.scrapper.models.entity.UpdatedLinkEntity;
import backend.academy.scrapper.repository.database.LinkUpdateRepository;
import backend.academy.scrapper.repository.database.jpa.mapper.UpdatedLinkMapper;
import backend.academy.scrapper.repository.database.jpa.repo.UpdatedLinkRepo;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "ORM")
public class JpaLinkUpdateRepository implements LinkUpdateRepository {
    private final UpdatedLinkRepo updatedLinkRepo;
    private final UpdatedLinkMapper mapper;

    @Override
    public List<UpdatedLink> findAll() {
        return updatedLinkRepo.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public UpdatedLink save(UpdatedLink updatedLink) {
        String chatIdsAsText = updatedLink.chatIds().stream()
            .map(chatId -> chatId.id().toString())
            .collect(Collectors.joining(","));

        UpdatedLinkEntity updatedLinkEntity = new UpdatedLinkEntity();
        updatedLinkEntity.linkId(updatedLink.id().id());
        updatedLinkEntity.uri(updatedLink.uri().toString());
        updatedLinkEntity.description(updatedLink.description());
        updatedLinkEntity.chatIds(chatIdsAsText);

        return mapper.toDomain(updatedLinkRepo.save(updatedLinkEntity));
    }

    @Override
    public UpdatedLink deleteById(LinkId id) {
        UpdatedLinkEntity updatedLink = updatedLinkRepo.findById(id.id()).orElseThrow();
        updatedLinkRepo.delete(updatedLink);
        return mapper.toDomain(updatedLink);
    }
}
