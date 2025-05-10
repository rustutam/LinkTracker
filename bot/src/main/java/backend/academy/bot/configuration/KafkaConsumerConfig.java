package backend.academy.bot.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.util.backoff.FixedBackOff;
import java.util.Map;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties properties;
    private final KafkaConfig config;

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Long, String>> defaultConsumerFactory(
            DefaultErrorHandler errorHandler, ConcurrentKafkaListenerContainerFactory<Long, String> factory) {
        factory.setConsumerFactory(consumerFactory(
                StringDeserializer.class,
                props -> props.put(
                        ConsumerConfig.GROUP_ID_CONFIG,
                        this.config.linkUpdates().consumerGroup())));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setCommonErrorHandler(errorHandler);
        factory.setAutoStartup(true);
        factory.setConcurrency(config.linkUpdates().concurrency());
        return factory;
    }

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(DeadLetterPublishingRecoverer dltPublisher) {
        FixedBackOff backOff = new FixedBackOff(500L, 1L);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(dltPublisher, backOff);
        errorHandler.setLogLevel(KafkaException.Level.DEBUG);
        errorHandler.setAckAfterHandle(true);
        errorHandler.setCommitRecovered(true);
        return errorHandler;
    }

    private <M> ConsumerFactory<Long, M> consumerFactory(
            Class<? extends Deserializer<M>> valueDeserializerClass, Consumer<Map<String, Object>> propsModifier) {
        var props = properties.buildConsumerProperties(null);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, LongDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, valueDeserializerClass);
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());

        propsModifier.accept(props);
        return new DefaultKafkaConsumerFactory<>(props);
    }
}
