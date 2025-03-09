package backend.academy.scrapper.models.entities;

import java.net.URI;
import java.time.OffsetDateTime;

public record LinksEntity(long linkId, URI linkUri, OffsetDateTime lastUpdateTime) {}
