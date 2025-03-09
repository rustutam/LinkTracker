package backend.academy.bot.service;

import static general.LogMessages.CHAT_ID;
import static general.LogMessages.URL;

import backend.academy.bot.configuration.BotConfig;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdateService {
    private final TelegramBot bot;

    public UpdateService(BotConfig botConfig) {
        bot = new TelegramBot(botConfig.telegramToken());
    }

    public void update(List<Long> tgChatIds, String url, String description) {
        try {
            tgChatIds.forEach(id -> {
                log.atInfo()
                        .addKeyValue(CHAT_ID, id)
                        .addKeyValue(URL, url)
                        .setMessage("Отправлено обновление")
                        .log();
                bot.execute(new SendMessage(
                        id, String.format("Пришло уведомление по url %s%nОписание: %s", url, description)));
            });
        } catch (Exception e) {
            log.atError()
                    .addKeyValue(CHAT_ID, tgChatIds.getFirst())
                    .addKeyValue(URL, url)
                    .setMessage("Некорректные параметры запроса при отправке обновления")
                    .log();
            throw new RuntimeException("Некорректные параметры запроса");
        }
    }
}
