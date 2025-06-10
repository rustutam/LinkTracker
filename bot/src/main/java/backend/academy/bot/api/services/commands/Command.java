package backend.academy.bot.api.services.commands;

import backend.academy.bot.api.tg.BotContext;
import backend.academy.bot.models.Update;

public interface Command {

    void execute(Update update, BotContext context);
}
