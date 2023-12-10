package edu.uoc.tfg.auction.application.request;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidRequest {

    @NotNull(message = "No has establecido la subasta de la puja")
    private Long auctionId;

    @NotNull(message = "El monto de la puja es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto de la puja debe ser mayor que cero")
    private BigDecimal amount;
}
