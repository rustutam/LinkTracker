package backend.academy.bot.api.services.commands;

import backend.academy.bot.api.dto.ApiErrorResponse;
import backend.academy.bot.api.services.scrapper.ApiScrapper;
import backend.academy.bot.api.tg.BotMessager;
import backend.academy.bot.api.tg.FSM;
import backend.academy.bot.api.tg.States;
import backend.academy.bot.api.tg.TgCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {
    private final ApiScrapper scrapper;
    private final BotMessager messager;

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean shouldBeExecuted(String command, FSM fsm) {
        return Objects.equals(command, TgCommand.START.cmdName());
    }

    @Override
    public void execute(Message message, FSM fsm, Map<Long, Map<String, String>> userData) {
        messager.sendMessage(message.chat().id(), "_This is start command!_", ParseMode.Markdown);
        fsm.setCurrentState(States.None);
        try {
            scrapper.registerChat(message.chat().id());
        } catch (ApiErrorResponse ex) {
            messager.sendMessage(
                    message.chat().id(), "_An error occured during request!_\n" + ex.description(), ParseMode.Markdown);
        }
    }
}
