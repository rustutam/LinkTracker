package backend.academy.bot.kafkaErrorHandlers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DltPublisher extends DeadLetterPublishingRecoverer {

    public DltPublisher(KafkaTemplate<Long, String> dltKafkaTemplate) {
        super(dltKafkaTemplate, (record, e) -> new TopicPartition(record.topic() + "-dlt", record.partition()));
    }

    @Override
    public void accept(ConsumerRecord<?, ?> record, Exception exception) {
        log.error(
                "Error processing message at {}, offset {}: {}",
                record.topic(),
                record.offset(),
                exception.getMessage());
        super.accept(record, exception);
    }
}
