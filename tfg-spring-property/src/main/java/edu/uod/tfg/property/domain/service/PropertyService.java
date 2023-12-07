package edu.uod.tfg.property.domain.service;

import edu.uod.tfg.property.domain.model.OwnerHistory;
import edu.uod.tfg.property.domain.model.Property;
import edu.uod.tfg.property.domain.model.PropertyStatus;

import java.util.List;
import java.util.Optional;

public interface PropertyService {
   Property createProperty(Property property);
   Property validateProperty(Long propertyId);
   Property invalidateProperty(Long propertyId);

   Property deleteProperty(Long propertyId, Long petitionUserId);
   Property changePropertyStatus(Long propertyId, PropertyStatus status);
   boolean cancelOwner(Long propertyId, Long userId);
   Long registerOwner(Long propertyId, Long userId);

   boolean sendChangePropertyRequest(Property property, String userRequestEmail);
   boolean isUserActualOwner(Long propertyId, Long userId);
   List<Property> findAllProperties();
   List<Property> findAllPendingValidation();
   List<Property> findAllPropertiesInAuction();
   List<Property> findActivePropertiesByOwner(Long userId);
   List<Property> findPropertiesByUserExcludingDeleted(Long userId);
   Optional<Property> findPropertyById(Long id);
   Optional<Property> findPropertyByCatastralReference(String reference);

   List<OwnerHistory> findAllOwnersHistory();
}
