package backend.academy.scrapper.sender;

import backend.academy.scrapper.client.TgBotClient;
import backend.academy.scrapper.models.LinkUpdateNotification;
import backend.academy.scrapper.models.api.LinkUpdate;
import backend.academy.scrapper.models.api.response.ApiErrorResponse;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class BotSenderImpl implements Sender {
    private final TgBotClient tgBotClient;

    @Override
    public void send(List<LinkUpdateNotification> linkUpdateNotifications) {
        for (LinkUpdateNotification linkUpdateNotification : linkUpdateNotifications) {
            tgBotClient.send(toLinkUpdateMapper(linkUpdateNotification));
        }
    }

    private void handleResponse(ResponseEntity<ApiErrorResponse> response) {
        int statusCode = response.getStatusCode().value();
        ApiErrorResponse body = response.getBody();

        switch (statusCode) {
            case 200 -> log.info("Успешный ответ от бота");
            case 400 -> log.info("Ошибка: " + body.exceptionMessage());
            default -> log.info("Неожиданный статус: " + statusCode);
        }
    }

    private LinkUpdate toLinkUpdateMapper(LinkUpdateNotification linkUpdateNotification) {
        return new LinkUpdate(
            new Random().nextLong(),
            linkUpdateNotification.uri(),
            "",
            linkUpdateNotification.chatIds()
        );
    }
}
