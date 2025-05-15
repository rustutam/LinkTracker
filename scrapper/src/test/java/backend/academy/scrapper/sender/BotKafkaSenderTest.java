// package backend.academy.scrapper.sender;
//
// import backend.academy.scrapper.scheduler.LinkUpdaterScheduler;
// import dto.LinkUpdate;
// import org.apache.kafka.clients.producer.ProducerConfig;
// import org.apache.kafka.common.serialization.StringSerializer;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.kafka.core.ConsumerFactory;
// import org.springframework.kafka.core.DefaultKafkaProducerFactory;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.test.annotation.DirtiesContext;
// import org.springframework.test.context.DynamicPropertyRegistry;
// import org.springframework.test.context.DynamicPropertySource;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;
// import org.testcontainers.kafka.ConfluentKafkaContainer;
// import java.util.List;
// import java.util.Map;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;
//
// @SpringBootTest
// @TestPropertySource(properties = "app.message-transport=Kafka")
// @Testcontainers
//// close consumers and producers after test
// @DirtiesContext(classMode = AFTER_CLASS)
// class BotKafkaSenderTest {
//
//    @Container
//    protected static final ConfluentKafkaContainer kafka = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.4.0");
//    @Autowired
//    private LinkUpdateSender sender;
//
//    KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(Map.of(
//        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29091, localhost:29092, localhost:29093",
//        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
//        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
//    )));
//
////    @Autowired
////    private KafkaTemplate<String, LinkUpdate> kafkaTemplate;
//
//    @MockitoBean
//    private LinkUpdaterScheduler linkUpdaterScheduler;
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
//    }
//    @Autowired
//    private ConsumerFactory<String, LinkUpdate> consumerFactory;
//
//    @Test
//    @DisplayName("Test of sending incorrect data to the topic")
//    void testOfSendingIncorrectDataToTheTopic() {
//        kafkaTemplate.send("scrapper.link_update", "hello").join();
//    }
//
//    @Test
//    @DisplayName("Test of sending correct data to the topic")
//    void testOfSendingCorrectDataToTheTopic() {
//        var update = new LinkUpdate(
//            1L,
//            "https://github.com/TheMLord/tinkoff-java-backend-course-2023-second-semester",
//            """
//                Added 1 branch(es):
//                createdAccount""",
//            List.of(667559701L)
//        );
//        sender.sendUpdates(update);
//
//
//    }
//
////    @Test
////    void shouldSendKafkaMessage() throws Exception {
////        kafkaTemplate.setConsumerFactory(consumerFactory);
////        LinkUpdate linkUpdate = new LinkUpdate(
////            1L,
////            "https://example.com",
////            "Test description",
////            List.of(1L,3L,6L)
////            );
////
////        var sendResult = kafkaTemplate.send("test-topic", linkUpdate)
////            .get();
////
////        var partition = sendResult.getRecordMetadata().partition();
////        var offset = sendResult.getRecordMetadata().offset();
////
////        var receivedMessage = kafkaTemplate.receive("test-topic", partition, offset)
////            .value();
////
////        assertEquals(linkUpdate, receivedMessage);
////    }
//
// }
