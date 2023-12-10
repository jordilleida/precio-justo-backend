package edu.uoc.tfg.auction.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataBidRepository extends JpaRepository<BidEntity, Long> {
}

