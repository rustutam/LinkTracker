package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.models.LinkMetadata;
import java.net.URI;
import java.util.List;

public interface ExternalDataRepository {
    List<LinkMetadata> getLinkLastUpdateDates(List<LinkMetadata> linkList);

    boolean isProcessingUri(URI uri);
}
