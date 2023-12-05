package edu.uod.tfg.property.domain.service;

import edu.uod.tfg.property.domain.model.OwnerHistory;
import edu.uod.tfg.property.domain.model.Property;
import edu.uod.tfg.property.domain.model.PropertyStatus;
import edu.uod.tfg.property.domain.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    @Override
    public Property createProperty(Property property) {

        Optional<Property> existingProperty = findPropertyByCatastralReference(property.getCatastralReference());

        if (existingProperty.isPresent()) {
            //Significa que la propiedad ya existe anteriormente
             Long existingPropertyId = existingProperty.get().getId();
             Long existingPropertyUserId = existingProperty.get().getUserId();

             cancelOwner(existingPropertyId, existingPropertyUserId);
        }

        property.setStatus(PropertyStatus.PENDING_VALIDATION);

        return propertyRepository.editOrCreateProperty(property);
    }
    @Override
    public Property validateProperty(Long propertyId) {
        Optional<Property> property = findPropertyById(propertyId);

                 Long propetyUserId = property.get().getUserId();

               //Nueva row de historyOwner con el nuevo propietario que figura en la propiedad
               registerOwner(propertyId, propetyUserId);

        return changePropertyStatus(propertyId, PropertyStatus.VALIDATED);
    }

    @Override
    public Property invalidateProperty(Long propertyId) {
        return changePropertyStatus(propertyId, PropertyStatus.DELETED);
    }

    @Override
    public Property changePropertyStatus(Long propertyId, PropertyStatus status) {
        return propertyRepository.setPropertyStatus(propertyId, status.toString());
    }

    @Override
    public boolean cancelOwner(Long propertyId, Long userId) {
        return propertyRepository.cancelOwner(propertyId, userId);
    }

    @Override
    public Long registerOwner(Long propertyId, Long userId) {
        Optional<Property> propertyOpt = propertyRepository.findPropertyById(propertyId);
        if (propertyOpt.isPresent()) {
            Property property = propertyOpt.get();
            if (propertyRepository.registerOwner(property, userId)) {
                return propertyId;
            }
        }
        return null;
    }

    @Override
    public List<Property> findAllProperties() {
        return propertyRepository.findAllProperties();
    }
    @Override
    public List<Property> findAllPendingValidation(){ return propertyRepository.findAllPendingValidation(); }
    @Override
    public List<Property> findAllPropertiesInAuction(){ return propertyRepository.findAllInAuction(); }
    @Override
    public List<Property> findActivePropertiesByOwner(Long userId) {
        return propertyRepository.findActivePropertiesByOwner(userId);
    }
    @Override
    public Optional<Property> findPropertyById(Long id) {
        return propertyRepository.findPropertyById(id);
    }

    @Override
    public Optional<Property> findPropertyByCatastralReference(String reference) {
        return propertyRepository.findPropertyByCatastralReference(reference);
    }

    @Override
    public List<OwnerHistory> findAllOwnersHistory(){
        return propertyRepository.findAllOwnersHistory();
    }
}
