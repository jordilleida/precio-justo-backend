package edu.uoc.tfg.auction.infrastructure.kafka;

import edu.uoc.tfg.auction.domain.messaging.MessageService;
import edu.uoc.tfg.auction.domain.model.Auction;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class KafkaAuctionMessagingService implements MessageService {

    private final KafkaTemplate<String, Auction> kafkaTemplate;

    public KafkaAuctionMessagingService(KafkaTemplate<String, Auction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendCreatedMessage(Auction auction) {
        String topicName = KafkaConstants.PROPERTY_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_CREATED;
        kafkaTemplate.send(topicName, auction);
        log.info("Sent kafka message to topic " + topicName + ": created auction " + auction.getId());
    }

    @Override
    public void sendFinishedMessage(Auction auction) {
        String topicName = KafkaConstants.PROPERTY_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_ENDED;
        kafkaTemplate.send(topicName, auction);
        log.info("Sent kafka message to topic " + topicName + ": finished auction " + auction.getId());
    }


}