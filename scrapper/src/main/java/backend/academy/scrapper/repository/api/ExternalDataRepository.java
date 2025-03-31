package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkChangeStatus;
import java.net.URI;

public abstract class ExternalDataRepository {
    abstract LinkChangeStatus getLinkChangeStatus(Link link);

    protected abstract boolean isProcessingUri(URI uri);
}
