package backend.academy.bot.client;

import backend.academy.bot.configuration.BotConfig;
import dto.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import static general.LogMessages.CHAT_ID;
import static general.LogMessages.STATUS;

@Component
@Slf4j
public class RegistrationClient {
    public static final String CHAT_REGISTERED = "Чат успешно зарегистрирован";

    private final RestClient restClient;

    public RegistrationClient(BotConfig botConfig) {
        this.restClient = RestClient.create(botConfig.baseUrl());
    }

    public String registerUser(Long chatId) {
        try {
            return restClient.post().uri("/tg-chat/{id}", chatId).exchange((request, response) -> {
                if (response.getStatusCode().is2xxSuccessful()) {
                    log.atInfo()
                            .addKeyValue(CHAT_ID, chatId)
                            .setMessage(CHAT_REGISTERED)
                            .log();
                    return CHAT_REGISTERED;
                } else {
                    ApiErrorResponse error = response.bodyTo(ApiErrorResponse.class);
                    if (error != null) {
                        log.atError()
                                .addKeyValue(CHAT_ID, chatId)
                                .addKeyValue(STATUS, response.getStatusCode())
                                .addKeyValue("description", error.description())
                                .setMessage("Не удалось зарегистрировать чат")
                                .log();
                        return error.description();
                    } else {
                        log.atError()
                                .addKeyValue(CHAT_ID, chatId)
                                .addKeyValue(STATUS, response.getStatusCode())
                                .setMessage("Не удалось зарегистрировать чат - Не удалось прочитать тело ответа")
                                .log();
                        return "Ошибка";
                    }
                }
            });
        } catch (Exception e) {
            return "Ошибка";
        }
    }
}
