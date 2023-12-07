package edu.uod.tfg.property.domain.repository;

import edu.uod.tfg.property.domain.model.OwnerHistory;
import edu.uod.tfg.property.domain.model.Property;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository {
    Property editOrCreateProperty(Property property);
    Property setPropertyStatus(Long propertyId, String status);

    boolean registerOwner(Property property, Long userId);
    boolean cancelOwner(Long propertyId, Long userId);
    List<Property> findAllProperties();
    List<Property> findAllPendingValidation();
    List<Property> findAllInAuction();
    List<Property> findActivePropertiesByOwner(Long userId);
    List<Property> findInAuctionPropertiesByOwner(Long userId);

    List<Property> findByUserIdAndStatusNot(Long userId, String deletedStatus);
    Optional<Property> findPropertyById(Long id);
    Optional<Property> findPropertyByCatastralReference(String reference);
    List<OwnerHistory> findAllOwnersHistory();
}
