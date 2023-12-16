package edu.uoc.tfg.auction.infrastructure.repository.jpa;

import edu.uoc.tfg.auction.domain.model.Auction;
import edu.uoc.tfg.auction.domain.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuctionRepositoryImpl implements AuctionRepository {
    @Autowired
    private SpringDataAuctionRepository auctionRepository;

    @Override
    public Auction editOrCreate(Auction auction) {

            AuctionEntity auctionEntity = AuctionEntity.fromDomain(auction);
            AuctionEntity savedEntity = auctionRepository.save(auctionEntity);

        return savedEntity.toDomain();
    }
    @Override
    public Optional<Auction> findCurrentAuctionByPropertyId(Long propertyId) {
        return auctionRepository.findActiveAuctionByPropertyId(propertyId)
                .map(AuctionEntity::toDomain);
    }

    @Override
    public Optional<Auction> findLastAuctionByPropertyId(Long propertyId) {
        return auctionRepository.findFirstByPropertyIdOrderByEndDateDesc(propertyId)
                .map(AuctionEntity::toDomain);
    }

    @Override
    public Optional<Auction> findAuctionById(Long auctionId) {

        return auctionRepository.findById(auctionId)
                .map(AuctionEntity::toDomain);
    }
    @Override
    public void updateWinningBid(Long auctionId, Long winningBidId) {
        auctionRepository.findById(auctionId).ifPresent(auctionEntity -> {
            auctionEntity.setWinningBidId(winningBidId);
            auctionRepository.save(auctionEntity);
        });
    }
    @Override
    public List<Auction> findAllAuctionsByStatus(String status) {
        return auctionRepository.findByStatus(status)
                .stream()
                .map(AuctionEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Auction> findAllAuctions() {
        return auctionRepository.findAll()
                .stream()
                .map(AuctionEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Auction> findActiveAuctionsPastEndDate() {
        return auctionRepository.findActiveAuctionsPastEndDate("ACTIVE")
                .stream()
                .map(AuctionEntity::toDomain)
                .collect(Collectors.toList());
    }
}
