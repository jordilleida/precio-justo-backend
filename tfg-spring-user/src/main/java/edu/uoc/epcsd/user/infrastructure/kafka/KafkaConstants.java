package edu.uoc.epcsd.user.infrastructure.kafka;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class KafkaConstants {

    // misc
    public static final String SEPARATOR = ".";

    // topic items
    public static final String USER_TOPIC = "users";

    // commands
    public static final String COMMAND_ADD = "add";

}
