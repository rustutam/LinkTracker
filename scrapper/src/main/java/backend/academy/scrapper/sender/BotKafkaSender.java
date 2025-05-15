package backend.academy.scrapper.sender;


import backend.academy.scrapper.configuration.KafkaConfig;
import backend.academy.scrapper.exceptions.ApiErrorResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.LinkUpdate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.message-transport", havingValue = "Kafka")
public class BotKafkaSender implements LinkUpdateSender {
    private final KafkaTemplate defaulKafkaTemplate;

    private final ObjectMapper objectMapper;
    private final KafkaConfig.LinkUpdatesTopicProperties topicProperties;

    @Override
    @SneakyThrows
    public void sendUpdates(LinkUpdate linkUpdate) throws ApiErrorResponseException {
        var data = objectMapper.writeValueAsString(linkUpdate);
        defaulKafkaTemplate.send(topicProperties.topic(), linkUpdate.id(), data);
        log.atInfo()
            .addKeyValue("link", linkUpdate.url())
            .setMessage("Отправка обновления по Kafka")
            .log();
    }
}
