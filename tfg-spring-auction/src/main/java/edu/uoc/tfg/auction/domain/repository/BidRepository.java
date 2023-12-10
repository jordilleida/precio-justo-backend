package edu.uoc.tfg.auction.domain.repository;

import edu.uoc.tfg.auction.domain.model.Bid;

public interface BidRepository {
    Bid createBid(Bid bid);
}
