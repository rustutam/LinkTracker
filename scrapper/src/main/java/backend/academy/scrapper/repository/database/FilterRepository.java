package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.exceptions.NotExistFilterException;
import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.ids.FilterId;
import java.util.Optional;

public interface FilterRepository {
    Optional<Filter> findById(FilterId filterId);

    Optional<Filter> findByFilter(String filter);

    Filter save(String filter);

    Filter deleteById(FilterId filterId) throws NotExistFilterException;
}
