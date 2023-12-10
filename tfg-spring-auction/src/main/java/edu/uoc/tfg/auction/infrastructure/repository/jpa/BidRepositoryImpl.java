package edu.uoc.tfg.auction.infrastructure.repository.jpa;

import edu.uoc.tfg.auction.domain.model.Bid;
import edu.uoc.tfg.auction.domain.repository.BidRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BidRepositoryImpl implements BidRepository {

    @Autowired
    private SpringDataBidRepository bidRepository;

    @Override
    @Transactional
    public Bid createBid(Bid bid) {

        BidEntity bidEntity = BidEntity.fromDomain(bid);
        BidEntity savedBidEntity = bidRepository.save(bidEntity);

        return savedBidEntity.toDomain();
    }
}
