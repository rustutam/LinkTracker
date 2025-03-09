package backend.academy.bot.commands;

import backend.academy.bot.client.TrackClient;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static general.LogMessages.CHAT_ID;

@RequiredArgsConstructor
@Component
@Slf4j
public class ListCommand implements Command {
    private final TelegramBot bot;
    private final TrackClient trackClient;

    @Override
    public void execute(Long chatId, String message) {
        log.atInfo()
            .addKeyValue(CHAT_ID, chatId)
            .setMessage("Выполняется команда /list")
            .log();
        bot.execute(new SendMessage(chatId, trackClient.getTrackLinks(chatId)));
    }

    @Override
    public String getName() {
        return CommandName.LIST.commandName();
    }
}
