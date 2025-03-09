package backend.academy.bot.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("RegexpSingleline")
public class BotMessages {
    public static final String HELP_MESSAGE =
            """
        Доступные команды:

        /start - Регистрация

        /help - Подробно поясняет что делает каждая команда

        /track - Начинает отслеживание ссылки.

        Всего доступны отслеживание 2 ссылок:
        1 - Отслеживания изменения репозитория GitHub
        2 - Отслеживание изменения ответов на вопрос StackOverflow

        Формат ссылок:
        Для GitHub - https://github.com/<owner>/<repo>

        Для StackOverflow - https://stackoverflow.com/questions/<question_id>

        Для ввода тегов - любые слова/символы через пробел
        Для ввода фильтров - фильтры в формате filter:filter

        /untrack - Прекращает отслеживание ссылки

        /list - Показывает список отслеживаемых ссылок""";

    public static final String WRONG_COMMAND = "Неизвестная команда";
}
