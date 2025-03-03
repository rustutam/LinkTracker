package backend.academy.scrapper.repository.api;

import backend.academy.scrapper.models.LinkInfo;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public interface ExternalDataRepository {
    Map<Long, OffsetDateTime> getLastUpdateDates(List<LinkInfo> linkInfoList);
}
