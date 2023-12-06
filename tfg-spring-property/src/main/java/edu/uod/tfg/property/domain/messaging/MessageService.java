package edu.uod.tfg.property.domain.messaging;

import edu.uod.tfg.property.domain.model.Property;

public interface MessageService {
    void sendValidateMessage(Property property);
    void sendDeletedMessage(Property property);

    void sendChangeRequestMessage(Property property, String userRequestEmail);
}

