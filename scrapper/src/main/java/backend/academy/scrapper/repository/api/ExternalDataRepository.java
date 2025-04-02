package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.models.domain.ChangeInfo;
import backend.academy.scrapper.models.domain.Link;
import java.net.URI;
import java.util.List;

public abstract class ExternalDataRepository {
    public abstract List<ChangeInfo> getChangeInfoByLink(Link link);

    protected abstract boolean isProcessingUri(URI uri);

}
