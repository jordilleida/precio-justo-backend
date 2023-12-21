package edu.uoc.tfg.auction.application.rest;

import edu.uoc.tfg.auction.application.request.AuctionRequest;
import edu.uoc.tfg.auction.application.request.BidRequest;
import edu.uoc.tfg.auction.domain.model.Auction;
import edu.uoc.tfg.auction.domain.model.AuctionConstants;
import edu.uoc.tfg.auction.domain.model.AuctionStatus;
import edu.uoc.tfg.auction.domain.service.AuctionService;
import javax.validation.Valid;

import edu.uoc.tfg.auction.infrastructure.security.CustomUserDetails;
import edu.uoc.tfg.auction.infrastructure.security.UserService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@CrossOrigin
public class AuctionRESTController {

    private final AuctionService auctionService;
    private final UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<String> createAuction(@Valid @RequestBody AuctionRequest auctionRequest) {

        Optional<CustomUserDetails> userDetails = userService.getAuthenticatedUser();

        if (!userDetails.isPresent())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Long authenticatedUserId = userDetails.get().getUserId();

        Auction newAuction = Auction.builder()
                .propertyId(auctionRequest.getPropertyId())
                .userId(authenticatedUserId)
                .initialPrice(auctionRequest.getInitialPrice())
                .status(AuctionStatus.ACTIVE)
                .endDate(LocalDateTime.now().plusDays(AuctionConstants.AUCTION_DURATION_DAYS))
                .build();

        Auction createdAuction = auctionService.createAuction(newAuction);

        if(createdAuction == null)
            return ResponseEntity.unprocessableEntity().body("No se pudo crear la subasta");

        return ResponseEntity.ok("Subasta creada con éxito con un precio de salida de " + createdAuction.getInitialPrice());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Auction>> getActiveAuctions() {

        List<Auction> activeAuctions = auctionService.getAllActiveAuctions();

        if(activeAuctions.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(activeAuctions);
    }

    @GetMapping("/ended")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<List<Auction>> getLastAuctions() {

        List<Auction> endedAuctions = auctionService.getLastAuctions();

        if(endedAuctions.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(endedAuctions);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Auction>> getAllAuctions() {

        List<Auction> allAuctions = auctionService.getAllAuctions();

        if(allAuctions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(allAuctions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auction> getAuctionById(@PathVariable Long id) {
        Optional<Auction> auction = auctionService.getAuctionById(id);

        if(!auction.isPresent()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(auction.get());
        }
    }

    @GetMapping("/property/{propertyId}/last")
    public ResponseEntity<Auction> getLastAuctionByPropertyId(@PathVariable Long propertyId) {

        Auction lastAuction = auctionService.getLastAuctionByPropertyId(propertyId);

        if (lastAuction == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(lastAuction);
        }
    }

    @GetMapping("/property/{propertyId}/current")
    public ResponseEntity<Auction> getCurrentAuctionByPropertyId(@PathVariable Long propertyId) {

        Auction currentAuction = auctionService.getCurrentAuctionByPropertyId(propertyId);

        if (currentAuction == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(currentAuction);
        }
    }
    @PostMapping("/bid")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity<String> placeBid(@Valid @RequestBody BidRequest bidRequest) {

        Optional<CustomUserDetails> userDetails = userService.getAuthenticatedUser();

        if (!userDetails.isPresent())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Long authenticatedUserId = userDetails.get().getUserId();

           BigDecimal savedAmount = auctionService.placeBid(bidRequest.getAuctionId(),
                                                  bidRequest.getAmount(),
                                                authenticatedUserId);

           if(savedAmount == null)
               return ResponseEntity.unprocessableEntity().body("La puja no es válida, pruebe de nuevo");

            return ResponseEntity.ok("Puja realizada correctamente por " + savedAmount + " €");

    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleIllegalStateAndArgument(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
