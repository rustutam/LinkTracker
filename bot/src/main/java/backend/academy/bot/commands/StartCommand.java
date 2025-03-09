package backend.academy.bot.commands;

import backend.academy.bot.client.RegistrationClient;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static general.LogMessages.CHAT_ID;

@RequiredArgsConstructor
@Component
@Slf4j
public class StartCommand implements Command {
    private final RegistrationClient registrationClient;
    private final TelegramBot bot;

    @Override
    public void execute(Long chatId, String message) {
        log.atInfo()
            .addKeyValue(CHAT_ID, chatId)
            .setMessage("Выполняется команда /start")
            .log();
        bot.execute(new SendMessage(chatId, registrationClient.registerUser(chatId)));
    }

    @Override
    public String getName() {
        return CommandName.START.commandName();
    }
}
