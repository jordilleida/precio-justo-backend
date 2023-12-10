package edu.uoc.tfg.auction.infrastructure.kafka;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class KafkaConstants {

    // misc
    public static final String SEPARATOR = ".";

    // topic items
    public static final String PROPERTY_TOPIC = "auction";

    // commands
    public static final String COMMAND_CREATED = "created";
    public static final String COMMAND_ENDED = "ended";

}
