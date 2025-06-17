package backend.academy.bot.api.services.commands;

import backend.academy.bot.api.tg.TgCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TgCommandFactory {
    private final StartCommand startCommand;
    private final HelpCommand helpCommand;
    private final ListCommand listCommand;
    private final TrackCommand trackCommand;
    private final UntrackCommand untrackCommand;
    private final UnknownCommand unknownCommand;

    public Command getTgCommand(String stringCommand){

        return switch (stringCommand) {
            case "/start" -> startCommand;
            case "/help" -> helpCommand;
            case "/list" -> listCommand;
            case "/track" -> trackCommand;
            case "/untrack" -> untrackCommand;
            default -> unknownCommand;
        };
    }
}
