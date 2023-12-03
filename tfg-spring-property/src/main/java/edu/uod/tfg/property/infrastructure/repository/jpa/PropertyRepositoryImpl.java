package edu.uod.tfg.property.infrastructure.repository.jpa;

import edu.uod.tfg.property.domain.model.Property;
import edu.uod.tfg.property.domain.model.PropertyImage;
import edu.uod.tfg.property.domain.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyRepositoryImpl implements PropertyRepository {
    @Autowired
    private SpringDataPropertyRepository propertyRepository;

    @Override
    public Property editOrCreateProperty(Property property) {
        PropertyEntity entity = PropertyEntity.fromDomain(property);
        entity = propertyRepository.save(entity);
        return entity.toDomain();
    }

    @Override
    public Property setPropertyImages(List<PropertyImage> images) {
        return null;
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
    public boolean cancelOwner(Long propertyId, Long userId) {
        return false;
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
