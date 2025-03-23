package backend.academy.scrapper.models.domain;

import backend.academy.scrapper.models.domain.ids.FilterId;

public record Filter(
    FilterId filterId,
    String value
) {
}
