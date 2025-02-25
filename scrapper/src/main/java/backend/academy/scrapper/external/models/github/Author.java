package backend.academy.scrapper.external.models.github;

import java.time.OffsetDateTime;

record Author(
    String name,
    String email,
    OffsetDateTime date
) {
}
