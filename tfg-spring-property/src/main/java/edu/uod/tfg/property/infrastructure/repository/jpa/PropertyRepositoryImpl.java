package edu.uod.tfg.property.infrastructure.repository.jpa;

import edu.uod.tfg.property.domain.model.OwnerHistory;
import edu.uod.tfg.property.domain.model.Property;
import edu.uod.tfg.property.domain.model.PropertyImage;
import edu.uod.tfg.property.domain.model.PropertyStatus;
import edu.uod.tfg.property.domain.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyRepositoryImpl implements PropertyRepository {
    @Autowired
    private SpringDataPropertyRepository propertyRepository;
    @Autowired
    private SpringDataOwnerHistoryRepository ownerHistoryRepository;
    @Override
    public Property editOrCreateProperty(Property property) {
        PropertyEntity entity = PropertyEntity.fromDomain(property);
        entity = propertyRepository.save(entity);
        return entity.toDomain();
    }
    @Override
    public Property setPropertyStatus(Long propertyId, String status) {

        Optional<PropertyEntity> entity = propertyRepository.findById(propertyId);

        if(!entity.isPresent())
            return null;

            PropertyEntity updatedEntity = entity.get();
            updatedEntity.setStatus(status);
            updatedEntity = propertyRepository.save(updatedEntity);
            return updatedEntity.toDomain();
    }

    @Override
    public boolean registerOwner(Property property, Long userId) {
        OwnerHistory newOwnerHistory = OwnerHistory.builder()
                .userId(userId)
                .property(property)
                .startDate(LocalDateTime.now())
                .endDate(null) // endDate es null ya que la propiedad está actualmente en posesión
                .build();

        OwnerHistoryEntity entity = OwnerHistoryEntity.fromDomain(newOwnerHistory);

        ownerHistoryRepository.save(entity);

        return true;
    }

    @Override
    public boolean cancelOwner(Long propertyId, Long userId) {

        Optional<OwnerHistoryEntity> ownerHistoryOptional = ownerHistoryRepository.findByUserIdAndProperty_Id(userId, propertyId);

        if (!ownerHistoryOptional.isPresent())
                                        return false;

        OwnerHistoryEntity ownerHistory = ownerHistoryOptional.get();

        ownerHistory.setEndDate(Optional.ofNullable(ownerHistory.getEndDate()).orElseGet(LocalDateTime::now));

        ownerHistoryRepository.save(ownerHistory);

        return true;
    }

    @Override
    public List<Property> findAllProperties() {
        return propertyRepository.findAll().stream()
                .map(PropertyEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnerHistory> findAllOwnersHistory() {
        return ownerHistoryRepository.findAll().stream()
                .map(OwnerHistoryEntity::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public List<Property> findAllPendingValidation() {
        String status = PropertyStatus.PENDING_VALIDATION.toString();
        return propertyRepository.findAllByStatus(status).stream()
                                .map(PropertyEntity::toDomain)
                                .collect(Collectors.toList());
    }
    @Override
    public List<Property> findAllInAuction() {
        String status = PropertyStatus.IN_AUCTION.toString();
        return propertyRepository.findAllByStatus(status).stream()
                .map(PropertyEntity::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public List<Property> findActivePropertiesByOwner(Long userId) {
        String status = PropertyStatus.VALIDATED.toString();
        return propertyRepository.findByUserIdAndStatus(userId, status).stream()
                .map(PropertyEntity::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public List<Property> findInAuctionPropertiesByOwner(Long userId) {
        String status = PropertyStatus.IN_AUCTION.toString();
        return propertyRepository.findByUserIdAndStatus(userId, status).stream()
                .map(PropertyEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Property> findByUserIdAndStatusNot(Long userId, String deletedStatus) {

        return propertyRepository.findByUserIdAndStatusNot(userId, deletedStatus).stream()
                .map(PropertyEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Property> findPropertyById(Long id) {
        return propertyRepository.findById(id)
                .map(PropertyEntity::toDomain);
    }

    @Override
    public Optional<Property> findPropertyByCatastralReference(String reference) {
        return propertyRepository.findByCatastralReference(reference)
                                  .map(PropertyEntity::toDomain);
    }
}
