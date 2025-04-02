package backend.academy.scrapper.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.annotation.Validated;

@Validated
@ComponentScan(basePackages = "general")
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ScrapperConfig(
        @Bean @NotNull Scheduler scheduler,
        @NotEmpty String githubToken,
        @NotEmpty String githubUri,
        StackOverflowCredentials stackOverflow,
        @NotEmpty String stackOverflowUri,
        @NotEmpty String baseUri,
        @NotNull int batchSize
) {
    public record StackOverflowCredentials(@NotEmpty String key, @NotEmpty String accessToken) {}

    public record Scheduler(@NotNull Duration interval) {}

    @Bean
    public ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }
}
