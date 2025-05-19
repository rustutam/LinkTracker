package backend.academy.bot;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@TestConfiguration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class KafkaConfiguration {
    private final KafkaProperties properties;

    @Bean
    public KafkaTemplate<Long, String> defaultKafkaTemplate() {
        var props = properties.buildProducerProperties(null);

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        var factory = new DefaultKafkaProducerFactory<Long, String>(props);
        return new KafkaTemplate<>(factory);
    }
}
