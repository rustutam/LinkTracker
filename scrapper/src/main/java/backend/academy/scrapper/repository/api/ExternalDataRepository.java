package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.models.LinkMetadata;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface ExternalDataRepository {
    List<LinkMetadata> getLinksWithNewLastUpdateDates(List<LinkMetadata> linkList);

    OffsetDateTime getLastUpdateDate(URI uri);

    boolean isProcessingUri(URI uri);
}
