package backend.academy.scrapper.repository.database.utilities.mapper;

import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.entities.FilterEntity;

public class FilterMapper {
    public static Filter toDomain(FilterEntity entity) {
        return new Filter(
            new FilterId(entity.id()),
            entity.value()
        );
    }
}
