package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.ids.FilterId;
import java.util.Optional;

public interface FilterRepository {
    Optional<Filter> findById(FilterId filterId);

    Optional<Filter> findByFilter(String filter);

    void save(String filter);

    void deleteById(FilterId filterId);
}
