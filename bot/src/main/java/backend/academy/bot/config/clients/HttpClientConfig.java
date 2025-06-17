package backend.academy.bot.config.clients;


import backend.academy.bot.config.BotConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class HttpClientConfig {

    @Bean
    public HttpComponentsClientHttpRequestFactory httpRequestFactory(BotConfig botConfig) {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(org.apache.hc.client5.http.config.RequestConfig.custom()
                        .setConnectTimeout(
                                Timeout.ofMilliseconds(botConfig.httpTimeout())) // Устанавливаем connectTimeout
                        .setResponseTimeout(
                                Timeout.ofMilliseconds(botConfig.httpTimeout())) // Устанавливаем readTimeout
                        .build())
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}
