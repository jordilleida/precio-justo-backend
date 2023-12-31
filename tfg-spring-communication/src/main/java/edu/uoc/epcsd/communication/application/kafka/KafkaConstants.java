package edu.uoc.epcsd.communication.application.kafka;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class KafkaConstants {

    // misc
    public static final String SEPARATOR = ".";

    // topic items
    public static final String USER_TOPIC = "users";
    public static final String PROPERTY_TOPIC = "property";
    // commands
    public static final String COMMAND_ADD = "add";
    public static final String COMMAND_VALIDATED = "validated";
    public static final String COMMAND_DELETED = "deleted";
    public static final String COMMAND_CHANGE_REQUEST = "change";

}
