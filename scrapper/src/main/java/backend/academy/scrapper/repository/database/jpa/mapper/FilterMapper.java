package backend.academy.scrapper.repository.database.jpa.mapper;

import backend.academy.scrapper.models.domain.Filter;
import backend.academy.scrapper.models.domain.ids.FilterId;
import backend.academy.scrapper.models.entity.FilterEntity;
import org.springframework.stereotype.Component;

@Component
public class FilterMapper {

    public Filter toDomain(FilterEntity filterEntity) {
        return new Filter(new FilterId(filterEntity.id()), filterEntity.filter(), filterEntity.createdAt());
    }

    public FilterEntity toEntity(Filter filter) {
        FilterEntity entity = new FilterEntity();
        if (filter.filterId() != null && filter.filterId().id() != 0) {
            entity.id(filter.filterId().id());
        }
        entity.filter(filter.value());
        entity.createdAt(filter.createdAt());
        return entity;
    }
}
