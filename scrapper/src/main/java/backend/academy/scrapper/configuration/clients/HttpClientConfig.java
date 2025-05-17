package backend.academy.scrapper.configuration.clients;

import backend.academy.scrapper.configuration.ScrapperConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class HttpClientConfig {

    @Bean
    public HttpComponentsClientHttpRequestFactory httpRequestFactory(ScrapperConfig scrapperConfig) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(scrapperConfig.httpTimeout());
        factory.setReadTimeout(scrapperConfig.httpTimeout());
        factory.setConnectionRequestTimeout(scrapperConfig.httpTimeout());
        return factory;
    }
}
