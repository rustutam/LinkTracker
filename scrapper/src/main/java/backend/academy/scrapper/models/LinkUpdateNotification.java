package backend.academy.scrapper.models;

import java.net.URI;
import java.util.List;

public record LinkUpdateNotification(long id, URI uri, List<Long> chatIds) {}
