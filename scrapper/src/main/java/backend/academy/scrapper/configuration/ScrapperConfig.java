package backend.academy.scrapper.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record ScrapperConfig(
        @NotNull Scheduler scheduler,
        @NotNull Integer batchSize,
        GitHubConfig github,
        StackOverflowConfig stackOverflow,
        TgBotConfig tgBot,
        KafkaConfig kafka,
        @Bean String messageTransport
) {
    public record Scheduler(@NotNull Duration interval) {}

    public record GitHubConfig(@NotEmpty String baseUri, @NotEmpty String token) {}

    public record StackOverflowConfig(@NotEmpty String baseUri, @NotEmpty String key, @NotEmpty String accessToken) {}

    public record TgBotConfig(@NotEmpty String baseUri) {}

    public record KafkaConfig(
        @NotBlank String bootstrapServers,
        @NotNull Topics topics) {

        public record Topics(
            @NotBlank String linkUpdates,
            @NotBlank String deadLetter) {}
    }
}
