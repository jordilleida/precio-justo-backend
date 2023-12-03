package edu.uod.tfg.property.domain.service;

import edu.uod.tfg.property.domain.model.Property;
import edu.uod.tfg.property.domain.model.PropertyStatus;

import java.util.List;
import java.util.Optional;

public interface PropertyService {
   Property createProperty(Property property);
   Property validateProperty(Property property);
   Property invalidateProperty(Property property);
   Property updatePropertyStatus(Long propertyId, PropertyStatus status);
   boolean cancelOwner(Long propertyId, Long userId);
   Long registerOwner(Long propertyId, Long userId);
   List<Property> findAllProperties();

   List<Property> findActivePropertiesByOwner(Long userId);

   Optional<Property> findPropertyById(Long id);
}
