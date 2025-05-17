package backend.academy.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record ScrapperConfig(
        @NotNull Integer batchSize,
        @Bean String messageTransport,
        Integer httpTimeout) {
}
