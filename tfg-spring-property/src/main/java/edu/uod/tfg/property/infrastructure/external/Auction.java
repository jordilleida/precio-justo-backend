package edu.uod.tfg.property.infrastructure.external;

import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auction {
    private Long id;
    private Long propertyId;

    private Long userId;

    private AuctionStatus status;
}
