package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.client.StackoverflowClient;
import backend.academy.scrapper.models.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import backend.academy.scrapper.models.LinkMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StackOverflowExternalDataRepository implements ExternalDataRepository {
    private final StackoverflowClient stackoverflowClient;

    @Override
    public List<LinkMetadata> getLinkLastUpdateDates(List<LinkMetadata> linkList) {
        return List.of();
    }

    @Override
    public boolean isProcessingUri(URI uri) {
        return uri.getHost().equals("stackoverflow.com");
    }
}
