package backend.academy.scrapper.repository.database.jpa;

import backend.academy.scrapper.exceptions.NotExistTagException;
import backend.academy.scrapper.models.domain.Tag;
import backend.academy.scrapper.models.domain.ids.TagId;
import backend.academy.scrapper.models.entity.TagEntity;
import backend.academy.scrapper.repository.database.TagRepository;
import backend.academy.scrapper.repository.database.jpa.mapper.TagMapper;
import backend.academy.scrapper.repository.database.jpa.repo.TagRepo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "ORM")
public class JpaTagRepository implements TagRepository {
    private final TagRepo tagRepo;
    private final TagMapper mapper;

    @Override
    public Optional<Tag> findById(TagId tagId) {
        return tagRepo.findById(tagId.id())
            .map(mapper::toDomain);
    }

    @Override
    public Optional<Tag> findByTag(String tag) {
        return tagRepo.findByTag(tag)
            .map(mapper::toDomain);
    }

    @Override
    public Tag save(String tag) {
        TagEntity tagEntity = new TagEntity();
        tagEntity.tag(tag);
        return mapper.toDomain(tagRepo.save(tagEntity));
    }

    @Override
    public Tag deleteById(TagId tagId) {
        TagEntity tagEntity = tagRepo.findById(tagId.id())
            .orElseThrow(NotExistTagException::new);
        tagRepo.delete(tagEntity);
        return mapper.toDomain(tagEntity);
    }
}
