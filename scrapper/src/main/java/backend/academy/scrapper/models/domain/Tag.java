package backend.academy.scrapper.models.domain;

import backend.academy.scrapper.models.domain.ids.TagId;

public record Tag(
    TagId tagId,
    String value
) {
}
