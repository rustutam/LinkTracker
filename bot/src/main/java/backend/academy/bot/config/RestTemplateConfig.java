package backend.academy.bot.config;

import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {
    private final BotConfig botConfig;

    @Bean
    public TelegramBot bot() {
        return new TelegramBot(botConfig.telegramToken());
    }

    @Bean
    public RestTemplateBuilder scrapperRestTemplateBuilder() {
        return new RestTemplateBuilder().rootUri(botConfig.scrapperHost());
    }
}
