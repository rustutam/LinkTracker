package backend.academy.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ApiClientsConfig {

    @Bean("githubRestClient")
    public RestClient gitHubRestClient(ScrapperConfig scrapperConfig) {
        return RestClient.builder()
            .defaultHeader("Authorization", "Bearer " + scrapperConfig.githubToken())
            .baseUrl(scrapperConfig.githubUri())
            .build();
    }

    @Bean("stackOverflowRestClient")
    public RestClient stackOverflowRestClient(ScrapperConfig scrapperConfig) {
        return RestClient.builder()
            .defaultHeader("Authorization", "key " + scrapperConfig.stackOverflow().key())
            .defaultHeader("Accept", "application/json")
            .baseUrl(scrapperConfig.stackOverflowUri())
            .build();
    }
}
