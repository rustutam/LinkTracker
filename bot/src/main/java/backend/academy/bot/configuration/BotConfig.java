package backend.academy.bot.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record BotConfig(
    @NotEmpty String telegramToken,
    @NotEmpty String baseUrl,
    @NotNull Kafka kafka
) {
    public record Kafka(@NotBlank String bootstrapServers,
                        @NotNull @NotBlank String updateTopicName,
                        @NotNull @NotBlank String dlqProcessingTopicName,
                        @NotNull @NotBlank String dlqDeserializerTopicName,
                        @NotNull @NotEmpty TopicsProperty[] topicsProperty) {
    }

    public record TopicsProperty(@NotBlank String topicName,
                                 @NotNull @Positive Integer numberPartitions,
                                 @NotNull @Positive Short replicationFactor) {

    }

}
