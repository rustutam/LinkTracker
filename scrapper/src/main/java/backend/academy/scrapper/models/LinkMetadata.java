package backend.academy.scrapper.models;

import java.net.URI;
import java.time.OffsetDateTime;

public record LinkMetadata(long id, URI linkUri, OffsetDateTime lastUpdateTime) {}
