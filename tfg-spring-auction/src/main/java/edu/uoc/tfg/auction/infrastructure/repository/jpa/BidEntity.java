package edu.uoc.tfg.auction.infrastructure.repository.jpa;

import edu.uoc.tfg.auction.domain.model.Bid;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bids")
public class BidEntity implements DomainTranslatable<Bid> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private BigDecimal amount;

    @Column(name = "date")
    @Builder.Default
    private LocalDateTime date = LocalDateTime.now();

    @Column(name = "auction_id")
    private Long auctionId;

    @Column(name = "user_id")
    private Long userId;
    @Override
    public Bid toDomain() {
        return Bid.builder()
                .id(this.id)
                .amount(this.amount)
                .userId(this.userId)
                .date(this.date)
                .auctionId(this.auctionId)
                .build();
    }

    public static BidEntity fromDomain(Bid bid) {
        return BidEntity.builder()
                .id(bid.getId())
                .amount(bid.getAmount())
                .userId(bid.getUserId())
                .date(bid.getDate())
                .auctionId(bid.getAuctionId() != null ? bid.getAuctionId() : null)
                .build();
    }
}
