package backend.academy.bot.api.services;

import backend.academy.bot.api.dto.LinkUpdate;
import backend.academy.bot.api.tg.BotSender;
import com.pengrad.telegrambot.model.request.ParseMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static backend.academy.bot.utils.LogMessages.CHAT_ID;
import static backend.academy.bot.utils.LogMessages.URL;
import static com.pengrad.telegrambot.model.MessageEntity.Type.url;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateServiceImpl implements UpdateService {
    private final BotSender botSender;

    @Override
    public void sendUpdate(LinkUpdate update) {
        String message = getMessage(update);
        for (Long chatId : update.tgChatIds()) {
            boolean isOkResponse = botSender.sendMessage(chatId, message, ParseMode.Markdown);

            if (isOkResponse) {
                log.atInfo()
                    .addKeyValue(CHAT_ID, chatId)
                    .addKeyValue(URL, url)
                    .setMessage("Отправлено обновление")
                    .log();
            } else {
                log.atError()
                    .addKeyValue(CHAT_ID, chatId)
                    .addKeyValue(URL, url)
                    .setMessage("Некорректные параметры запроса при отправке сообщения об обновлении")
                    .log();
            }
        }
    }

    private String getMessage(LinkUpdate update) {
        return new StringBuilder()
            .append("Пришло обновление по ссылке: ")
            .append(update.url())
            .append("\n")
            .append("Описание: \n")
            .append(update.description())
            .toString();
    }
}
