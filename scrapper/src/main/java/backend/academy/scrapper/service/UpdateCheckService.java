package backend.academy.scrapper.service;

import backend.academy.scrapper.models.domain.Link;
import backend.academy.scrapper.models.domain.LinkChangeStatus;

public interface UpdateCheckService {
    LinkChangeStatus detectChanges(Link link);
}
