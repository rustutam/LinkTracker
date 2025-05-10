package backend.academy.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CommonKafkaConfig {
    private final KafkaProperties kafkaProperties;

    @Bean
    Admin localKafkaClusterAdminClient() {
        return AdminClient.create(
                Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers()));
    }

    @Bean
    KafkaAdmin localKafkaClusterAdmin() {
        return new KafkaAdmin(
                Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers()));
    }
}
