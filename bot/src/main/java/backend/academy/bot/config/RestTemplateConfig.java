package backend.academy.bot.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    @Autowired
    private BotConfig botConfig;

    @Bean
    public TelegramBot bot() {
        return new TelegramBot(botConfig.telegramToken());
    }

    @Bean
    public RestTemplateBuilder scrapperRestTemplateBuilder() {
        return new RestTemplateBuilder()
            .rootUri(botConfig.scrapperHost())
            .connectTimeout(Duration.ofMillis(botConfig.httpTimeout()))
            .readTimeout(Duration.ofMillis(botConfig.httpTimeout()));
    }
}
