package edu.uoc.tfg.auction.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auction {
    private Long id;
    private Long propertyId;

    private Long userId;
    private LocalDateTime endDate;
    private BigDecimal initialPrice;

    private AuctionStatus status;
    private Long winningBidId;
    private List<Bid> bids;
}
