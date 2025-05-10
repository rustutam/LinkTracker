package backend.academy.scrapper.configuration.updates_sending;

import backend.academy.scrapper.configuration.ScrapperConfig;
import backend.academy.scrapper.sender.BotHttpSender;
import backend.academy.scrapper.sender.LinkUpdateSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "message-transport", havingValue = "Http")
public class HttpSenderConfig {
    @Bean
    public LinkUpdateSender httpUpdateSender(
        ScrapperConfig scrapperConfig
    ) {
        return new BotHttpSender(scrapperConfig);
    }
}
