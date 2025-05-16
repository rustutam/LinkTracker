package backend.academy.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {
    private final ScrapperConfig scrapperConfig;

    @Bean
    public long checkUpdateIntervalMs() {
        return scrapperConfig.scheduler().checkUpdateInterval().toMillis();
    }

    @Bean
    public long sendUpdateIntervalMs() {
        return scrapperConfig.scheduler().sendUpdateInterval().toMillis();
    }
}
