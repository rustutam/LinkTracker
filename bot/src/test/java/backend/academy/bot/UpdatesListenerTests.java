package backend.academy.bot;

import backend.academy.bot.api.kafka.UpdatesMessageConsumer;
import backend.academy.bot.api.services.UpdatesService;
import backend.academy.bot.config.KafkaConsumerConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = {"link-updates", "link-updates-dlt"})
@Import({TestcontainersConfiguration.class, KafkaConfiguration.class, KafkaConsumerConfig.class})
public class UpdatesListenerTests {
    @MockitoBean
    private UpdatesService mockUpdatesService;

    @Autowired
    private KafkaTemplate<Long, String> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private UpdatesMessageConsumer consumer;

    @Test
    public void checkConsumeMethod() throws InterruptedException, ExecutionException, IOException {
        String topic = "link-update";
        String message =
                Files.readString(new ClassPathResource("message.json").getFile().toPath());
        kafkaTemplate.send(topic, message).get();

        Thread.sleep(10000);

        verify(mockUpdatesService, timeout(15000)).notifySubscribers(any());
    }
}
