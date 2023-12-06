package edu.uod.tfg.property.infrastructure.kafka;

import edu.uod.tfg.property.domain.messaging.MessageService;
import edu.uod.tfg.property.domain.model.Property;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class KafkaPropertyMessagingService implements MessageService {

    private final KafkaTemplate<String, Property> kafkaTemplate;

    public KafkaPropertyMessagingService(KafkaTemplate<String, Property> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendValidateMessage(Property property) {
        String topicName = KafkaConstants.PROPERTY_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_VALIDATED;
        kafkaTemplate.send(topicName, property);
        log.info("Sent kafka message to topic " + topicName + " for user " +
                 property.getContact() + " property validated " + property.getId());
    }

    @Override
    public void sendDeletedMessage(Property property) {
        String topicName = KafkaConstants.PROPERTY_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_DELETED;
        kafkaTemplate.send(topicName, property);
        log.info("Sent kafka message to topic " + topicName + " for user " +
                property.getContact() + " property not validated " + property.getId());
    }
    @Override
    public void sendChangeRequestMessage(Property property, String userRequestEmail) {
        String topicName = KafkaConstants.PROPERTY_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_CHANGE_REQUEST;
        kafkaTemplate.send(topicName, property);
        log.info("Sent kafka message to topic " + topicName + " for user " +
                property.getContact() + " property change id " + property.getId() +
                " user request " + userRequestEmail );
    }

}
