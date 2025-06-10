package backend.academy.bot.config;

import com.pengrad.telegrambot.TelegramBot;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.validation.annotation.Validated;

@Validated
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ConfigurationProperties(prefix = "app")
public record BotConfig(@NotEmpty String telegramToken, Integer httpTimeout) {
    @Bean
    public TelegramBot bot() {
        return new TelegramBot(telegramToken());
    }
}
