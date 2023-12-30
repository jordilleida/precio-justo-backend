package edu.uoc.tfg.auction;

import edu.uoc.tfg.auction.domain.model.Auction;
import edu.uoc.tfg.auction.domain.model.AuctionConstants;
import edu.uoc.tfg.auction.domain.model.AuctionStatus;
import edu.uoc.tfg.auction.domain.model.Bid;
import edu.uoc.tfg.auction.domain.repository.AuctionRepository;
import edu.uoc.tfg.auction.domain.repository.BidRepository;
import edu.uoc.tfg.auction.domain.service.AuctionServiceImpl;
import edu.uoc.tfg.auction.infrastructure.kafka.KafkaAuctionMessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuctionServiceUnitTest {

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private BidRepository bidRepository;

    @Mock
    private KafkaAuctionMessagingService kafkaAuctionMessagingService;

    @InjectMocks
    private AuctionServiceImpl auctionService;

    private Auction createMockAuction() {
        return Auction.builder()
                .id(1L)
                .propertyId(2L)
                .userId(3L)
                .initialPrice(new BigDecimal("100.00"))
                .status(AuctionStatus.ACTIVE)
                .endDate(LocalDateTime.now().plusDays(AuctionConstants.AUCTION_DURATION_DAYS))
                .build();
    }
    private Bid createMockBid(Long auctionId, BigDecimal amount) {
        return Bid.builder()
                .id(1L)
                .auctionId(auctionId)
                .userId(4L)
                .amount(amount)
                .date(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateAuction() {
        Auction mockAuction = createMockAuction();
        when(auctionRepository.editOrCreate(any(Auction.class))).thenReturn(mockAuction);

        doNothing().when(kafkaAuctionMessagingService).sendCreatedMessage(any(Auction.class));

        Auction result = auctionService.createAuction(mockAuction);
        assertNotNull(result);
        assertEquals(mockAuction.getPropertyId(), result.getPropertyId());
    }

    @Test
    void testUpdateAuction() {
        Auction auctionToUpdate = createMockAuction();
        when(auctionRepository.editOrCreate(auctionToUpdate)).thenReturn(auctionToUpdate);

        Auction updatedAuction = auctionService.updateAuction(auctionToUpdate);
        assertNotNull(updatedAuction);
        verify(auctionRepository).editOrCreate(auctionToUpdate);
    }
    @Test
    void testPlaceBid() {
        Long auctionId = 1L;
        BigDecimal bidAmount = new BigDecimal("200.00");
        Auction mockAuction = createMockAuction();
        mockAuction.setBids(Arrays.asList(createMockBid(auctionId, new BigDecimal("150.00"))));

        when(auctionRepository.findAuctionById(auctionId)).thenReturn(Optional.of(mockAuction));
        when(bidRepository.createBid(any(Bid.class))).thenReturn(createMockBid(auctionId, bidAmount));

        BigDecimal placedBidAmount = auctionService.placeBid(auctionId, bidAmount, 4L);
        assertEquals(bidAmount, placedBidAmount);


    }
    @Test
    void testPlaceBidWithInactiveAuction() {
        Long auctionId = 1L;
        BigDecimal bidAmount = new BigDecimal("200.00");
        Auction mockAuction = createMockAuction();
        mockAuction.setStatus(AuctionStatus.ENDED);

        when(auctionRepository.findAuctionById(auctionId)).thenReturn(Optional.of(mockAuction));

        assertThrows(IllegalStateException.class, () ->
                auctionService.placeBid(auctionId, bidAmount, 4L)
        );
    }

    @Test
    void testPlaceBidLowerThanCurrent() {
        Long auctionId = 1L;
        BigDecimal currentBidAmount = new BigDecimal("200.00");
        BigDecimal newBidAmount = new BigDecimal("150.00");
        Auction mockAuction = createMockAuction();
        mockAuction.setBids(Arrays.asList(createMockBid(auctionId, currentBidAmount)));

        when(auctionRepository.findAuctionById(auctionId)).thenReturn(Optional.of(mockAuction));

        assertThrows(IllegalArgumentException.class, () ->
                auctionService.placeBid(auctionId, newBidAmount, 4L)
        );
    }
    @Test
    void testGetAllActiveAuctions() {
        List<Auction> activeAuctions = Arrays.asList(
                createMockAuctionWithStatus(AuctionStatus.ACTIVE),
                createMockAuctionWithStatus(AuctionStatus.ACTIVE)
        );
        when(auctionRepository.findAllAuctionsByStatus(AuctionStatus.ACTIVE.toString())).thenReturn(activeAuctions);

        List<Auction> returnedAuctions = auctionService.getAllActiveAuctions();
        assertEquals(activeAuctions.size(), returnedAuctions.size());

    }

    private Auction createMockAuctionWithStatus(AuctionStatus status) {
        return Auction.builder()
                .id(1L)
                .propertyId(2L)
                .userId(3L)
                .initialPrice(new BigDecimal("100.00"))
                .status(status)
                .endDate(LocalDateTime.now().plusDays(AuctionConstants.AUCTION_DURATION_DAYS))
                .build();
    }
    @Test
    void testGetLastAuctions() {
        List<Auction> endedAuctions = Arrays.asList(
                createMockAuctionWithStatus(AuctionStatus.ENDED),
                createMockAuctionWithStatus(AuctionStatus.ENDED)
        );
        when(auctionRepository.findAllAuctionsByStatus(AuctionStatus.ENDED.toString())).thenReturn(endedAuctions);

        List<Auction> returnedAuctions = auctionService.getLastAuctions();
        assertEquals(endedAuctions.size(), returnedAuctions.size());

    }
    @Test
    void testGetAuctionById() {
        Long auctionId = 1L;
        Auction mockAuction = createMockAuction();
        when(auctionRepository.findAuctionById(auctionId)).thenReturn(Optional.of(mockAuction));

        Optional<Auction> foundAuction = auctionService.getAuctionById(auctionId);
        assertTrue(foundAuction.isPresent());
        assertEquals(mockAuction, foundAuction.get());

        // Test de cuando la subasta no se encuentra
        when(auctionRepository.findAuctionById(anyLong())).thenReturn(Optional.empty());
        Optional<Auction> notFoundAuction = auctionService.getAuctionById(2L);
        assertFalse(notFoundAuction.isPresent());
    }
    @Test
    void testGetCurrentAuctionByPropertyId() {
        Long propertyId = 2L;
        Auction mockAuction = createMockAuction();
        when(auctionRepository.findCurrentAuctionByPropertyId(propertyId)).thenReturn(Optional.of(mockAuction));

        Auction currentAuction = auctionService.getCurrentAuctionByPropertyId(propertyId);
        assertNotNull(currentAuction);
        assertEquals(mockAuction.getId(), currentAuction.getId());
    }

    @Test
    void testGetLastAuctionByPropertyId() {
        Long propertyId = 2L;
        Auction mockAuction = createMockAuction();
        when(auctionRepository.findLastAuctionByPropertyId(propertyId)).thenReturn(Optional.of(mockAuction));

        Auction lastAuction = auctionService.getLastAuctionByPropertyId(propertyId);
        assertNotNull(lastAuction);
        assertEquals(mockAuction.getId(), lastAuction.getId());
    }
}
