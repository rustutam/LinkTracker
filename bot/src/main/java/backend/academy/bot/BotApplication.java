package backend.academy.bot;

import backend.academy.bot.config.BotConfig;
import backend.academy.bot.config.KafkaConfig;
import backend.academy.bot.config.RetryCodesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "backend.academy.bot.api.controllers")
@EnableConfigurationProperties({BotConfig.class, KafkaConfig.class, RetryCodesConfig.class})
public class BotApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }
}
