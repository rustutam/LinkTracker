package backend.academy.bot.commands;

public interface Command {
    void execute(Long chatId, String message);

    String getName();
}
