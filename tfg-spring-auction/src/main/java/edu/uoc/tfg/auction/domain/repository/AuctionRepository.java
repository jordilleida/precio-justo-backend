package edu.uoc.tfg.auction.domain.repository;

import edu.uoc.tfg.auction.domain.model.Auction;

import java.util.List;
import java.util.Optional;

public interface AuctionRepository {
    Auction editOrCreate(Auction auction);
    Optional<Auction> findCurrentAuctionByPropertyId(Long propertyId);

    Optional<Auction> findLastAuctionByPropertyId(Long propertyId);
    Optional<Auction> findAuctionById(Long auctionId);
    void updateWinningBid(Long auctionId, Long winningBidId);
    List<Auction> findAllAuctionsByStatus(String status);

    List<Auction> findAllAuctions();
    List<Auction> findActiveAuctionsPastEndDate();

}
