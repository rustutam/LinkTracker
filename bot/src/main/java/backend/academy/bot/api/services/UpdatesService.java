package backend.academy.bot.api.services;

import backend.academy.bot.api.dto.LinkUpdate;

public interface UpdatesService {
    void notifySubscribers(LinkUpdate update);
}
