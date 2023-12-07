package edu.uoc.epcsd.user.application.kafka;
import edu.uoc.epcsd.user.domain.Role;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.service.RoleService;
import edu.uoc.epcsd.user.domain.service.UserService;
import edu.uoc.epcsd.user.infrastructure.external.Property;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j2
@Component
public class KafkaUserClassListener {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @KafkaListener(topics = KafkaListenerConstants.PROPERTY_TOPIC + KafkaListenerConstants.SEPARATOR + KafkaListenerConstants.COMMAND_VALIDATED,
                            groupId = "user-group",
                            containerFactory = "userKafkaListenerContainerFactory")

    public void propertyValidated(Property property) {
        log.trace("Property Validated in User Service");

        // Lógica para agregar rol seller si la propiedad es validada
        try {
            Optional<User> userOptional = userService.findUserById(property.getUserId());

            if (userOptional.isEmpty()) {
                log.info("User not found: " + property.getUserId());
            }

            User user = userOptional.get();

            Role sellerRole = roleService.getSellerRole();

            if (!user.getRoles().contains(sellerRole)){

                user.getRoles().add(sellerRole);

                userService.updateUser(user);
            }

        } catch (Exception e) {
            log.error("Error adding SELLER role to user: " + e.getMessage());

        }
    }
}