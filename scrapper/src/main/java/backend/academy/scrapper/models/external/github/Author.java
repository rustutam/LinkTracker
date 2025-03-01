package backend.academy.scrapper.models.external.github;

import java.time.OffsetDateTime;

record Author(
    String name,
    String email,
    OffsetDateTime date
) {
}
