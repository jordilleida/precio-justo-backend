package edu.uoc.tfg.auction.domain.service;

import edu.uoc.tfg.auction.domain.model.Auction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AuctionService {

    Auction createAuction(Auction newAuction);
    Auction updateAuction(Auction updatedAuction);

    Optional<Auction> getAuctionById(Long auctionId);
    Auction getCurrentAuctionByPropertyId(Long propertyId);

    Auction getLastAuctionByPropertyId(Long propertyId);

    List<Auction> getAllAuctions();
    List<Auction> getAllActiveAuctions();
    List<Auction> getLastAuctions();
    void updateAuctionStatuses();
    BigDecimal placeBid(Long auctionId, BigDecimal amount, Long userId);

}
