package backend.academy.scrapper;

import backend.academy.scrapper.configuration.KafkaConfig;
import backend.academy.scrapper.configuration.RetryCodesConfig;
import backend.academy.scrapper.configuration.SchedulerConfig;
import backend.academy.scrapper.configuration.ScrapperConfig;
import backend.academy.scrapper.configuration.clients.BotConfig;
import backend.academy.scrapper.configuration.clients.GitHubConfig;
import backend.academy.scrapper.configuration.clients.StackOverflowConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties({
    ScrapperConfig.class,
    GitHubConfig.class,
    StackOverflowConfig.class,
    BotConfig.class,
    SchedulerConfig.class,
    KafkaConfig.class,
    RetryCodesConfig.class,
})
public class ScrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}
