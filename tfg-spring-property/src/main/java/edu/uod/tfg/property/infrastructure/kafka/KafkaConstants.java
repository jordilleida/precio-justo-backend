package edu.uod.tfg.property.infrastructure.kafka;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class KafkaConstants {

    // misc
    public static final String SEPARATOR = ".";

    // topic items
    public static final String PROPERTY_TOPIC = "property";

    // commands
    public static final String COMMAND_VALIDATED = "validated";
    public static final String COMMAND_DELETED = "deleted";
    public static final String COMMAND_CHANGE_REQUEST = "change";

}