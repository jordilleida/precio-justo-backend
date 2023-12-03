package edu.uod.tfg.property.domain.repository;

import edu.uod.tfg.property.domain.model.Property;
import edu.uod.tfg.property.domain.model.PropertyImage;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository {
    Property editOrCreateProperty(Property property);

    Property setPropertyImages(List<PropertyImage> images);

    Property setPropertyStatus(Long propertyId, String status);

    boolean cancelOwner(Long propertyId, Long userId);

    List<Property> findAllProperties();

    List<Property> findActivePropertiesByOwner(Long userId);

    Optional<Property> findPropertyById(Long id);
}
