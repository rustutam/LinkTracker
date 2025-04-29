//package backend.academy.scrapper.configuration;
//
//import backend.academy.scrapper.client.GithubClient;
//import backend.academy.scrapper.client.StackoverflowClient;
//import backend.academy.scrapper.client.TgBotClient;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@RequiredArgsConstructor
//public class ClientsConfig {
//    private final GitHubConfig gitHubConfig;
//    private final StackOverflowConfig stackOverflowConfig;
//    private final TgBotConfig tgBotConfig;
//
//    @Bean
//    public GithubClient gitHubRestClient() {
//        return new GithubClient(gitHubConfig);
//    }
//
//    @Bean
//    public StackoverflowClient stackOverflowRestClient() {
//        return new StackoverflowClient(stackOverflowConfig);
//    }
//
//    @Bean
//    public TgBotClient tgBotRestClient() {
//        return new TgBotClient(tgBotConfig);
//    }
//}
