package backend.academy.bot;

import static org.assertj.core.api.Assertions.assertThat;

import backend.academy.bot.api.kafka.UpdatesMessageConsumer;
import backend.academy.bot.config.KafkaConsumerConfig;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = {"link-update", "link-update-dlt"})
@Import({TestcontainersConfiguration.class, KafkaConfiguration.class, KafkaConsumerConfig.class})
public class DltTests {
    @Autowired
    private KafkaTemplate<Long, String> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private UpdatesMessageConsumer consumer;

    @Test
    public void checkDeadLetter() throws InterruptedException, ExecutionException {

        String topic = "link-update";
        String dltTopic = topic + "-dlt";
        String message = "hello world";
        kafkaTemplate.send(topic, message).get();

        Thread.sleep(10000);

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        ConsumerFactory<Long, String> cf =
                new DefaultKafkaConsumerFactory<>(consumerProps, new LongDeserializer(), new StringDeserializer());
        var consumer = cf.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, dltTopic);

        ConsumerRecord<Long, String> received = KafkaTestUtils.getSingleRecord(consumer, dltTopic);
        assertThat(received.value()).isEqualTo(message);
        consumer.close();
    }
}
