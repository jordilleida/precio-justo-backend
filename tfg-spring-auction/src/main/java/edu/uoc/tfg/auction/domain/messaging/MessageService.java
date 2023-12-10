package edu.uoc.tfg.auction.domain.messaging;

import edu.uoc.tfg.auction.domain.model.Auction;

public interface MessageService {
    void sendCreatedMessage(Auction auction);
    void sendFinishedMessage(Auction auction);
}
