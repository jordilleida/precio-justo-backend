package edu.uoc.tfg.auction.infrastructure.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic auctionCreatedTopic() {
        return new NewTopic(KafkaConstants.PROPERTY_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_CREATED, 1, (short) 1);
    }
    @Bean
    public NewTopic auctionFinishedTopic() {
        return new NewTopic(KafkaConstants.PROPERTY_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_ENDED, 1, (short) 1);
    }
}
