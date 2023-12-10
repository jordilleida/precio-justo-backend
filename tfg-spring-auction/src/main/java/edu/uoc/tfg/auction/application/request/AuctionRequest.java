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
public class AuctionRequest {

    @NotNull(message = "Falta la propiedad sobre la que se iniciará la subasta")
    private Long propertyId;

    @NotNull(message = "El precio inicial es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio inicial debe ser mayor que cero")
    private BigDecimal initialPrice;
}