package edu.uoc.tfg.auction.infrastructure.repository.jpa;

import edu.uoc.tfg.auction.domain.model.Auction;
import edu.uoc.tfg.auction.domain.model.AuctionStatus;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@ToString(exclude = "bids")
@Getter
@Setter
@EqualsAndHashCode(exclude = "bids")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auctions")
public class AuctionEntity implements DomainTranslatable<Auction> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "property_id")
    @NotNull
    private Long propertyId;

    @Column(name = "user_id")
    @NotNull
    private Long userId;
    @Column(name = "start_date")
    @Builder.Default
    private LocalDateTime startDate = LocalDateTime.now();

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "initial_price")
    @NotNull
    private BigDecimal initialPrice;

    private String status;

    @Column(name = "winning_bid")
    private Long winningBidId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "auction_id")
    private Set<BidEntity> bids = new HashSet<>();

    @Override
    public Auction toDomain() {
        return Auction.builder()
                .id(this.id)
                .propertyId(this.propertyId)
                .userId(this.userId)
                .endDate(this.endDate)
                .initialPrice(this.initialPrice)
                .status(AuctionStatus.valueOf(this.status))
                .winningBidId(this.winningBidId)
                .bids(this.bids != null ? this.bids.stream()
                        .map(BidEntity::toDomain)
                        .collect(Collectors.toList())
                        : null)
                .build();
    }

    public static AuctionEntity fromDomain(Auction auction) {
        Set<BidEntity> bidEntities = null;
        if (auction.getBids() != null) {
            bidEntities = auction.getBids().stream()
                    .map(BidEntity::fromDomain)
                    .collect(Collectors.toSet());
        }

        return AuctionEntity.builder()
                .id(auction.getId())
                .propertyId(auction.getPropertyId())
                .userId(auction.getUserId())
                .endDate(auction.getEndDate())
                .initialPrice(auction.getInitialPrice())
                .status(auction.getStatus().toString())
                .winningBidId(auction.getWinningBidId())
                .bids(bidEntities)
                .build();
    }
}
