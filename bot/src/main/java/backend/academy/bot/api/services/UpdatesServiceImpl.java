package backend.academy.bot.api.services;

import backend.academy.bot.api.dto.LinkUpdate;
import backend.academy.bot.api.tg.BotMessager;
import com.pengrad.telegrambot.model.request.ParseMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdatesServiceImpl implements UpdatesService {
    private final BotMessager messager;

    public UpdatesServiceImpl(BotMessager messager) {
        this.messager = messager;
    }

    @Override
    public void notifySubscribers(LinkUpdate update) {
        for (Long chatId : update.tgChatIds()) {
            if (!messager.sendMessage(
                    chatId,
                    "Link " + update.url() + " was updated!\n" + "Description: " + update.description(),
                    ParseMode.Markdown)) {
                log.error("Cannot send message to {}", chatId);
            }
        }
    }
}
