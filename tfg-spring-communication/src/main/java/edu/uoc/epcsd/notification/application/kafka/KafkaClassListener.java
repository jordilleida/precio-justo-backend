package edu.uoc.epcsd.notification.application.kafka;

import edu.uoc.epcsd.notification.domain.User;
import edu.uoc.epcsd.notification.domain.service.NotificationService;
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
}
