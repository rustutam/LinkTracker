package backend.academy.bot.config;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = false)
public record KafkaConfig(LinkUpdatesTopicProperties linkUpdates) {
    public record LinkUpdatesTopicProperties(
            @NotEmpty String topic,
            @NotEmpty boolean failOnProcessing,
            @NotEmpty String consumerGroup,
            @NotEmpty Integer concurency) {}
}
