package edu.uoc.tfg.auction.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bid {
    private Long id;
    private Long auctionId;

    private Long userId;
    private BigDecimal amount;
    private LocalDateTime date;

}
