package backend.academy.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.scheduler")
public record SchedulerConfig(
    @NotNull Duration checkUpdateInterval,
    @NotNull Duration sendUpdateInterval
) {

    @Bean
    public long checkUpdateIntervalMs() {
        return checkUpdateInterval.toMillis();
    }

    @Bean
    public long sendUpdateIntervalMs() {
        return sendUpdateInterval.toMillis();
    }
}
