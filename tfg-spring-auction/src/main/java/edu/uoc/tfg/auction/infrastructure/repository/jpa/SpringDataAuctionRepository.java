package edu.uoc.tfg.auction.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataAuctionRepository extends JpaRepository<AuctionEntity, Long> {
    Optional<AuctionEntity> findFirstByPropertyIdOrderByEndDateDesc(Long propertyId);
    List<AuctionEntity> findByStatus(String status);

    @Query("SELECT a FROM AuctionEntity a WHERE a.propertyId = :propertyId AND a.status = 'ACTIVE' ORDER BY a.endDate DESC")
    Optional<AuctionEntity> findActiveAuctionByPropertyId(@Param("propertyId") Long propertyId);
    @Query("SELECT a FROM AuctionEntity a WHERE a.status = :status AND a.endDate < CURRENT_TIMESTAMP")
    List<AuctionEntity> findActiveAuctionsPastEndDate(@Param("status") String status);

}
