package backend.academy.scrapper.service;

import backend.academy.scrapper.models.LinkMetadata;
import java.util.List;

public interface ChangesDetectService {
    List<LinkMetadata> detectChanges();
}
