package backend.academy.scrapper.configuration;

import backend.academy.scrapper.client.GithubClient;
import backend.academy.scrapper.client.StackoverflowClient;
import backend.academy.scrapper.client.TgBotClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientsConfig {

    @Bean("gitHubClient")
    public GithubClient gitHubRestClient(GitHubConfig gitHubConfig) {
        return new GithubClient(gitHubConfig);
    }

    @Bean("stackOverflowClient")
    public StackoverflowClient stackOverflowRestClient(StackOverflowConfig stackOverflowConfig) {
        return new StackoverflowClient(stackOverflowConfig);
    }

    @Bean("tgBotClient")
    public TgBotClient tgBotRestClient(TgBotConfig tgBotConfig) {
        return new TgBotClient(tgBotConfig);
    }
}

