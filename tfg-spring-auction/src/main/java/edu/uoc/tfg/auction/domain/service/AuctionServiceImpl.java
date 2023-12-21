package edu.uoc.tfg.auction.domain.service;

import edu.uoc.tfg.auction.domain.model.Auction;
import edu.uoc.tfg.auction.domain.model.AuctionStatus;
import edu.uoc.tfg.auction.domain.model.Bid;
import edu.uoc.tfg.auction.domain.repository.AuctionRepository;
import edu.uoc.tfg.auction.domain.repository.BidRepository;
import javax.ws.rs.NotFoundException;

import edu.uoc.tfg.auction.infrastructure.kafka.KafkaAuctionMessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class AuctionServiceImpl implements AuctionService{

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    private final KafkaAuctionMessagingService kafkaAuctionMessagingService;

    @Override
    public Auction createAuction(Auction newAuction){

        //Si hay una subasta actualmente activa, no se puede crear otra, devuelve la actual
        Auction currentAuction = getCurrentAuctionByPropertyId(newAuction.getPropertyId());

        if(currentAuction != null){
            return currentAuction;
        }

        Auction createdAuction = auctionRepository.editOrCreate(newAuction);

        kafkaAuctionMessagingService.sendCreatedMessage(createdAuction);

        return createdAuction;
    }

    @Override
    public Auction updateAuction(Auction updatedAuction){
        return auctionRepository.editOrCreate(updatedAuction);
    }

    @Override
    public Auction getCurrentAuctionByPropertyId(Long propertyId) {
        return auctionRepository.findCurrentAuctionByPropertyId(propertyId)
                .orElse(null);
    }

    @Override
    public Auction getLastAuctionByPropertyId(Long propertyId) {
        return auctionRepository.findLastAuctionByPropertyId(propertyId)
                .orElse(null);
    }

    @Override
    public List<Auction> getAllAuctions() {
        return auctionRepository.findAllAuctions();
    }

    @Override
    public Optional<Auction> getAuctionById(Long auctionId){
        return auctionRepository.findAuctionById(auctionId);
    }
    @Override
    public List<Auction> getAllActiveAuctions() {
        return auctionRepository.findAllAuctionsByStatus(AuctionStatus.ACTIVE.toString());
    }

    @Override
    public List<Auction> getLastAuctions() {
        return auctionRepository.findAllAuctionsByStatus(AuctionStatus.ENDED.toString());
    }
    @Override
    public BigDecimal placeBid(Long auctionId, BigDecimal amount, Long userId){

        Auction auction = auctionRepository.findAuctionById(auctionId).orElseThrow(
                () -> new NotFoundException("Subasta no encontrada : " + auctionId)
        );

        // Verificamos si la auction está activa y si no ha finalizado
        if (!auction.getStatus().equals(AuctionStatus.ACTIVE) ||
                auction.getEndDate().isBefore(LocalDateTime.now())) {

            throw new IllegalStateException("La subasta no está activa o ya ha finalizado.");
        }

        // Obtengo la última puja de la subasta
        Bid lastBid = auction.getBids().stream()
                .max(Comparator.comparing(Bid::getDate))
                .orElse(null);

        //Verifico si la puja es mayor que el precio inicial
        if(amount.compareTo(auction.getInitialPrice()) <= 0)
            throw new IllegalArgumentException("La puja debe ser mayor que el precio inicial.");

        // Verifico si la nueva puja es mayor que la última puja
        if (lastBid != null && amount.compareTo(lastBid.getAmount()) <= 0) {
            throw new IllegalArgumentException("La nueva puja debe ser mayor que la última puja.");
        }

        // Crear un nuevo objeto Bid
        Bid newBid = Bid.builder()
                .auctionId(auctionId)
                .amount(amount)
                .userId(userId)
                .date(LocalDateTime.now())
                .build();

        // Guardar la nueva puja en el repositorio
          Bid createdBid = bidRepository.createBid(newBid);

          auctionRepository.updateWinningBid(auctionId, createdBid.getId());

          return createdBid.getAmount();
    }

    @Override
    @Scheduled(fixedRate = 60000) // Se ejecuta cada minuto (60000 ms)
    public void updateAuctionStatuses() {

        List<Auction> auctionsToUpdate = auctionRepository.findActiveAuctionsPastEndDate();


        for (Auction auction : auctionsToUpdate) {
            if (auction.getBids().isEmpty()) {

                log.info("Subasta finalizada sin pujas: {}", auction.getId());
                auction.setStatus(AuctionStatus.NO_BIDS);

            } else {

                log.info("Subasta finalizada: {}", auction.getId());
                auction.setStatus(AuctionStatus.ENDED);

            }

            //Aqui debo notificar via kafka para que el microservicio de property haga lo propio
            kafkaAuctionMessagingService.sendFinishedMessage(auction);

            updateAuction(auction);

        }
    }
}
