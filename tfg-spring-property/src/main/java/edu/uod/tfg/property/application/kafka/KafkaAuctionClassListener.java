package edu.uod.tfg.property.application.kafka;

import edu.uod.tfg.property.domain.model.Property;
import edu.uod.tfg.property.domain.model.PropertyStatus;
import edu.uod.tfg.property.domain.service.PropertyService;
import edu.uod.tfg.property.infrastructure.external.Auction;
import edu.uod.tfg.property.infrastructure.external.AuctionStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j2
@Component
public class KafkaAuctionClassListener {

    @Autowired
    private PropertyService propertyService;


    @KafkaListener(topics = KafkaListenerConstants.PROPERTY_TOPIC + KafkaListenerConstants.SEPARATOR + KafkaListenerConstants.COMMAND_CREATED,
            groupId = "auction-group",
            containerFactory = "auctionKafkaListenerContainerFactory")

    public void auctionCreated(Auction auction) {
        log.trace("Auction created for property "+ auction.getPropertyId());

        // Lógica para indicar que una propiedad esta en subasta
        try {
            Long propertyId = auction.getPropertyId();

               Optional<Property> propertyOptional = propertyService.findPropertyById(propertyId);

                if (propertyOptional.isEmpty()) {
                    log.info("Property not found: " + propertyId);
                }

                  propertyService.changePropertyStatus(propertyId, PropertyStatus.IN_AUCTION);

        } catch (Exception e) {
            log.error("Error changing property status by created auction : " + e.getMessage());

        }
    }
    @KafkaListener(topics = KafkaListenerConstants.PROPERTY_TOPIC + KafkaListenerConstants.SEPARATOR + KafkaListenerConstants.COMMAND_ENDED,
            groupId = "auction-group",
            containerFactory = "auctionKafkaListenerContainerFactory")

    public void auctionFinished(Auction auction) {
        log.trace("Auction finished for property "+ auction.getPropertyId() + " with status" + auction.getStatus().toString());

        // Lógica para indicar que una propiedad ya no esta en subasta
        try {
            Long propertyId = auction.getPropertyId();

            Optional<Property> propertyOptional = propertyService.findPropertyById(propertyId);

            if (propertyOptional.isEmpty()) {
                log.info("Property not found: " + propertyId);
            }

            if(auction.getStatus().equals(AuctionStatus.ENDED)) {
                propertyService.changePropertyStatus(propertyId, PropertyStatus.SOLD);
            }
            if(auction.getStatus().equals(AuctionStatus.NO_BIDS)){
                propertyService.changePropertyStatus(propertyId, PropertyStatus.VALIDATED);
            }

        } catch (Exception e) {
            log.error("Error changing property status by finished auction : " + e.getMessage());

        }
    }
}
