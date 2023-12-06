package edu.uoc.epcsd.communication.application.kafka;

import edu.uoc.epcsd.communication.domain.Property;
import edu.uoc.epcsd.communication.domain.service.NotificationService;
import edu.uoc.epcsd.communication.domain.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class KafkaClassListener {

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = KafkaConstants.USER_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_ADD, groupId = "group-1")
    void userAdded(User user) {

        log.trace("User Added");

        notificationService.notifyUserRegistered(user);
    }

    @KafkaListener(topics = KafkaConstants.PROPERTY_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_VALIDATED, groupId = "group-2",
                   containerFactory = "propertyKafkaListenerContainerFactory")
    void propertyValidated(Property property) {
        log.trace("Property Validated");

        notificationService.notifyPropertyValidated(property);
    }

    @KafkaListener(topics = KafkaConstants.PROPERTY_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_DELETED, groupId = "group-2",
            containerFactory = "propertyKafkaListenerContainerFactory")
    void propertyDeleted(Property property) {
        log.trace("Property deleted");

        notificationService.notifyPropertyDeleted(property);
    }

    @KafkaListener(topics = KafkaConstants.PROPERTY_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_CHANGE_REQUEST, groupId = "group-2",
            containerFactory = "propertyKafkaListenerContainerFactory")
    void propertyChangeRequest(Property property) {
        log.trace("Property change");

        notificationService.notifyChangeRequest(property);
    }

}
