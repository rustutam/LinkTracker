package backend.academy.bot.client;

import backend.academy.bot.configuration.BotConfig;
import dto.request.AddLinkRequest;
import dto.response.ApiErrorResponse;
import dto.response.ListLinksResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import static general.LogMessages.CHAT_ID;
import static general.LogMessages.LINK;
import static general.LogMessages.STATUS;

@Component
@Slf4j
public class TrackClient {
    public static final String ERROR = "Ошибка";
    public static final String TG_CHAT_ID_STRING = "Tg-Chat-Id";
    public static final String LINKS_COMMAND_STRING = "/links";
    public static final String GITHUB_COM_STRING = "github.com";

    private final RestClient restClient;

    public TrackClient(BotConfig botConfig) {
        this.restClient = RestClient.create(botConfig.baseUrl());
    }

    public String trackLink(Long chatId, String link, List<String> tags, List<String> filters) {
        try {
            return restClient
                .post()
                .uri(LINKS_COMMAND_STRING)
                .header(TG_CHAT_ID_STRING, chatId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AddLinkRequest(link, tags, filters))
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        if (link.contains(GITHUB_COM_STRING)) {
                            log.atInfo()
                                .addKeyValue(CHAT_ID, chatId)
                                .addKeyValue(LINK, link)
                                .setMessage("Изменения в репозитории у чата теперь отслеживаются")
                                .log();
                            return "Изменения в репозитории теперь отслеживаются";
                        } else {
                            log.atInfo()
                                .addKeyValue(CHAT_ID, chatId)
                                .addKeyValue(LINK, link)
                                .setMessage("Новые ответы на вопрос у чата теперь отслеживаются")
                                .log();
                            return "Новые ответы на вопрос теперь отслеживаются";
                        }
                    } else {
                        log.atError()
                            .addKeyValue(LINK, link)
                            .addKeyValue(CHAT_ID, chatId)
                            .setMessage("Произошла ошибка при отслеживании ссылки")
                            .log();
                        return Optional.ofNullable(response.bodyTo(ApiErrorResponse.class))
                            .map(ApiErrorResponse::description)
                            .orElse(ERROR);
                    }
                });
        } catch (Exception e) {
            return "Ошибка";
        }
    }

    public String unTrackLink(Long chatId, String link) {
        try {
            return restClient
                .method(HttpMethod.DELETE)
                .uri(LINKS_COMMAND_STRING)
                .header(TG_CHAT_ID_STRING, chatId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(LINK, link))
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        if (link.contains(GITHUB_COM_STRING)) {
                            log.atInfo()
                                .addKeyValue(CHAT_ID, chatId)
                                .addKeyValue(LINK, link)
                                .setMessage("Изменения в репозитории у чата теперь больше не отслеживаются")
                                .log();
                            return "Изменения в репозитории больше не отслеживается";
                        } else {
                            log.atInfo()
                                .addKeyValue(CHAT_ID, chatId)
                                .addKeyValue(LINK, link)
                                .setMessage("Изменения в вопросе у чата больше не отслеживаются")
                                .log();
                            return "Изменения в вопросе больше не отслеживаются";
                        }
                    } else {
                        ApiErrorResponse error = response.bodyTo(ApiErrorResponse.class);
                        if (error != null) {
                            log.atError()
                                .addKeyValue(CHAT_ID, chatId)
                                .addKeyValue(LINK, link)
                                .addKeyValue(STATUS, response.getStatusCode())
                                .addKeyValue("error", error.description())
                                .setMessage("Не удалось прекратить отслеживание ссылки")
                                .log();
                            return error.description();
                        } else {
                            log.atError()
                                .addKeyValue(CHAT_ID, chatId)
                                .addKeyValue(LINK, link)
                                .addKeyValue(STATUS, response.getStatusCode())
                                .setMessage(
                                    "Не удалось прекратить отслеживание ссылки: Тело ответа не удалось прочитать")
                                .log();
                            return ERROR;
                        }
                    }
                });
        } catch (Exception e) {
            return "Ошибка";
        }
    }

    public String getTrackLinks(Long chatId) {
        try {
            return restClient
                .get()
                .uri(LINKS_COMMAND_STRING)
                .header(TG_CHAT_ID_STRING, String.valueOf(chatId))
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        ListLinksResponse dto = response.bodyTo(ListLinksResponse.class);
                        if (dto != null) {
                            String result = dto.toString();
                            log.atInfo()
                                .addKeyValue(CHAT_ID, chatId)
                                .addKeyValue("links", result)
                                .setMessage("Успешный ответ от /links")
                                .log();
                            return result;
                        } else {
                            log.atWarn()
                                .addKeyValue(CHAT_ID, chatId)
                                .setMessage("Успешный ответ от /links, но тело пустое или невалидное")
                                .log();
                            return "Не удалось получить список отслеживаемых ссылок";
                        }
                    } else {
                        ApiErrorResponse error = response.bodyTo(ApiErrorResponse.class);
                        if (error != null) {
                            log.atError()
                                .addKeyValue(CHAT_ID, chatId)
                                .addKeyValue(STATUS, response.getStatusCode())
                                .addKeyValue("description", error.description())
                                .setMessage("Ошибка при запросе /links")
                                .log();
                            return error.description();
                        } else {
                            log.atError()
                                .addKeyValue(CHAT_ID, chatId)
                                .addKeyValue(STATUS, response.getStatusCode())
                                .setMessage("Ошибка при запросе /links, тело не удалось прочитать")
                                .log();
                            return "Произошла ошибка";
                        }
                    }
                });
        } catch (Exception e) {
            return "Ошибка";
        }
    }
}
