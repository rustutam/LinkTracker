package backend.academy.scrapper.service;

import backend.academy.scrapper.models.Link;
import java.util.List;

public interface ChangesDetectService {
    List<Link> detectChanges();
}
