package backend.academy.scrapper.configuration.clients;

import backend.academy.scrapper.configuration.ScrapperConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class HttpClientConfig {

    @Bean
    public HttpComponentsClientHttpRequestFactory httpRequestFactory(ScrapperConfig scrapperConfig) {
        CloseableHttpClient httpClient = HttpClients.custom()
            .setDefaultRequestConfig(org.apache.hc.client5.http.config.RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(scrapperConfig.httpTimeout())) // Устанавливаем connectTimeout
                .setResponseTimeout(Timeout.ofMilliseconds(scrapperConfig.httpTimeout())) // Устанавливаем readTimeout
                .build())
            .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}
