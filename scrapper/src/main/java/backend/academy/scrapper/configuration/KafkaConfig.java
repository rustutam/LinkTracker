package backend.academy.scrapper.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@ConfigurationProperties(prefix = "kafka")
public record KafkaConfig(@Bean LinkUpdatesTopicProperties linkUpdates) {
    public record LinkUpdatesTopicProperties(
            @NotEmpty String topic, @NotEmpty int partitions, @NotEmpty short replicas) {}
}
