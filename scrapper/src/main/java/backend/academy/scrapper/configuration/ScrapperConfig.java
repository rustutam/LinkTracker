package backend.academy.scrapper.configuration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ScrapperConfig(
    @Bean
    @NotNull Scheduler scheduler,
    @NotEmpty String githubToken,
    @NotEmpty String githubUri,
    StackOverflowCredentials stackOverflow,
    @NotEmpty String stackOverflowUri
) {
    public record StackOverflowCredentials(@NotEmpty String key, @NotEmpty String accessToken) {
    }

    public record Scheduler(@NotNull Duration interval) {
    }
}
