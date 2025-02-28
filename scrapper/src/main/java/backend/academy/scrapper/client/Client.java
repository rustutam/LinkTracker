package backend.academy.scrapper.client;

import java.net.URI;
import java.time.OffsetDateTime;

public interface Client {
    OffsetDateTime getLastUpdateDate(String repoUrl);
}
