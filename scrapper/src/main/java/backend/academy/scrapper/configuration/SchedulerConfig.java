package backend.academy.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {
    private final ScrapperConfig scrapperConfig;

    @Bean("schedulerIntervalMs")
    public long schedulerIntervalMs() {
        return scrapperConfig.scheduler().interval().toMillis();
    }
}
