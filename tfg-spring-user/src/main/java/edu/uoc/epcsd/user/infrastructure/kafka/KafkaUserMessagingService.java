package edu.uoc.epcsd.user.infrastructure.kafka;

import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.messaging.MessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class KafkaUserMessagingService implements MessageService {

    private final KafkaTemplate<String, User> kafkaTemplate;

    public KafkaUserMessagingService(KafkaTemplate<String, User> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(User user) {
        String topicName = KafkaConstants.USER_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_ADD;
        kafkaTemplate.send(topicName, user);
        log.info("Sent kafka message to topic " + topicName + " for user " + user.getEmail());
    }
}
