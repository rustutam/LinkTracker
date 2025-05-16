package backend.academy.scrapper.service;

import java.time.OffsetDateTime;

public interface LinkProcessingService {
    void processLinks(Integer limit, OffsetDateTime updateStartTime);
}
