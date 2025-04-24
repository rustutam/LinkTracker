package backend.academy.scrapper.repository.database.jpa.mapper;

import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.entity.FilterEntity;

public class FilterMapper {
    public static Filter map(FilterEntity filterEntity) {
        return new Filter(
            new FilterId(filterEntity.id()),
            filterEntity.filter()
        );
    }
}
