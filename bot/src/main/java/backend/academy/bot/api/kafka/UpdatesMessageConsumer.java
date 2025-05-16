package backend.academy.bot.api.kafka;

import backend.academy.bot.api.dto.LinkUpdate;
import backend.academy.bot.api.services.UpdatesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdatesMessageConsumer {

    private final UpdatesService updatesService;
    private final ObjectMapper objectMapper;

    @KafkaListener(containerFactory = "defaultConsumerFactory", topics = "${kafka.link-updates.topic}")
    public void consume(ConsumerRecord<Long, String> record, Acknowledgment acknowledgment)
            throws JsonProcessingException {
        updatesService.notifySubscribers(objectMapper.readValue(record.value(), LinkUpdate.class));
        acknowledgment.acknowledge();
    }
}
