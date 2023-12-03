package edu.uod.tfg.property.domain.service;

import edu.uod.tfg.property.domain.model.Property;
import edu.uod.tfg.property.domain.model.PropertyStatus;

import java.util.List;
import java.util.Optional;

public class PropertyServiceImpl implements PropertyService {
    @Override
    public Property createProperty(Property property) {
            //Ver si insertando imagenes y luego la propiedad o la propiedad de una vez, si inserta en images o que

        return null;
    }
    @Override
    public Property validateProperty(Property property) {
        return null;
    }

    @Override
    public Property invalidateProperty(Property property) {
        return null;
    }

    @Override
    public Property updatePropertyStatus(Long propertyId, PropertyStatus status) {
        return null;
    }

    @Override
    public boolean cancelOwner(Long propertyId, Long userId) {
        return true;
    }

    @Override
    public Long registerOwner(Long propertyId, Long userId) {
        return null;
    }

    @Override
    public List<Property> findAllProperties() {
        return null;
    }

    @Override
    public List<Property> findActivePropertiesByOwner(Long userId) {
        return null;
    }

    @Override
    public Optional<Property> findPropertyById(Long id) {
        return Optional.empty();
    }
}
