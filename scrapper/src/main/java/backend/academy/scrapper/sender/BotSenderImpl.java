package backend.academy.scrapper.sender;

import backend.academy.scrapper.client.TgBotClient;
import backend.academy.scrapper.models.domain.LinkUpdateNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class BotSenderImpl extends Sender {
    private final TgBotClient tgBotClient;

    @Override
    public void send(LinkUpdateNotification linkUpdateNotification) {
        tgBotClient.send(toLinkUpdateMapper(linkUpdateNotification));
    }
//TODO убрать комменты
    //    private void handleResponse(ResponseEntity<ApiErrorResponse> response) {
    //        int statusCode = response.getStatusCode().value();
    //        ApiErrorResponse body = response.getBody();
    //
    //        switch (statusCode) {
    //            case 200 -> log.info("Успешный ответ от бота");
    //            case 400 -> log.info("Ошибка: " + body.exceptionMessage());
    //            default -> log.info("Неожиданный статус: " + statusCode);
    //        }
    //    }

}
