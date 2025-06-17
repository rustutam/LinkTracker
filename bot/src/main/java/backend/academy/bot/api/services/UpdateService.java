package backend.academy.bot.api.services;

import backend.academy.bot.api.dto.LinkUpdate;

public interface UpdateService {
    void sendUpdate(LinkUpdate update);
}
