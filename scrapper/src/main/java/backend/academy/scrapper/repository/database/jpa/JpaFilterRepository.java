package backend.academy.scrapper.repository.database.jpa;

import backend.academy.scrapper.exceptions.NotExistFilterException;
import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.entity.FilterEntity;
import backend.academy.scrapper.repository.database.FilterRepository;
import backend.academy.scrapper.repository.database.jpa.mapper.FilterMapper;
import backend.academy.scrapper.repository.database.jpa.repo.FilterRepo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "access-type", havingValue = "ORM")
public class JpaFilterRepository implements FilterRepository {
    private final FilterRepo filterRepo;
    private final FilterMapper mapper;

    @Override
    public Optional<Filter> findById(FilterId filterId) {
        return filterRepo.findById(filterId.id()).map(mapper::toDomain);
    }

    @Override
    public Optional<Filter> findByFilter(String filter) {
        return filterRepo.findByFilter(filter).map(mapper::toDomain);
    }

    @Override
    public Filter save(String filter) {
        FilterEntity filterEntity = new FilterEntity();
        filterEntity.filter(filter);
        return mapper.toDomain(filterRepo.save(filterEntity));
    }

    @Override
    public Filter deleteById(FilterId filterId) throws NotExistFilterException {
        FilterEntity filterEntity = filterRepo.findById(filterId.id()).orElseThrow(NotExistFilterException::new);
        filterRepo.delete(filterEntity);
        return mapper.toDomain(filterEntity);
    }
}
