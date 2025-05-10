package backend.academy.bot.listener;

import backend.academy.bot.configuration.BotConfig;
import backend.academy.bot.service.UpdateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.LinkUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaLinkUpdateListener {
    private final UpdateService updateService;
    private final ObjectMapper objectMapper;

    @KafkaListener(containerFactory = "defaultConsumerFactory", topics = "${kafka.link-updates.topic}")
    public void consume
        (ConsumerRecord<Long, String> record,
         Acknowledgment acknowledgment
        ) throws JsonProcessingException {
        LinkUpdate linkUpdate = objectMapper.readValue(record.value(), LinkUpdate.class);
        updateService.update(linkUpdate.tgChatIds(), linkUpdate.url(), linkUpdate.description());
        acknowledgment.acknowledge();
    }


}
