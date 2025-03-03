package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.client.StackoverflowClient;
import backend.academy.scrapper.models.LinkInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class StackOverflowExternalDataRepository implements ExternalDataRepository {
    private final StackoverflowClient stackoverflowClient;

    @Override
    public Map<Long, OffsetDateTime> getLastUpdateDates(List<LinkInfo> linkInfoList) {
        return Map.of();
    }
}
