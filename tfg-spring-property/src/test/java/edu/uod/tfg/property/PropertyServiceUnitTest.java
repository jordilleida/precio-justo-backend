package edu.uod.tfg.property;

import edu.uod.tfg.property.domain.model.Property;
import edu.uod.tfg.property.domain.model.PropertyStatus;
import edu.uod.tfg.property.domain.model.PropertyType;
import edu.uod.tfg.property.domain.repository.PropertyRepository;
import edu.uod.tfg.property.domain.service.PropertyServiceImpl;
import edu.uod.tfg.property.infrastructure.kafka.KafkaPropertyMessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PropertyServiceUnitTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private KafkaPropertyMessagingService kafkaPropertyMessagingService;

    @InjectMocks
    private PropertyServiceImpl propertyService;

    private Property createMockProperty() {
        Property property = new Property();
        property.setId(1L);
        property.setType(PropertyType.VIVIENDA);
        property.setDescription("Piso en Barcelona");
        property.setRooms(3);
        property.setBaths(2);
        property.setSurface(120.5f);
        property.setStatus(PropertyStatus.PENDING_VALIDATION);
        property.setRegistryDocumentUrl("https://example.com/registry.pdf");
        property.setCatastralReference("12345BCN");
        property.setUserId(4L);
        property.setLatitude(41.38879);
        property.setLongitude(2.15899);
        property.setAddress("Calle Falsa 123, Barcelona");
        return property;
    }

    @Test
    void testCreateProperty() {
        Property mockProperty = createMockProperty();
        when(propertyRepository.editOrCreateProperty(any(Property.class))).thenReturn(mockProperty);

        Property result = propertyService.createProperty(mockProperty);
        assertNotNull(result);
        assertEquals(PropertyStatus.PENDING_VALIDATION, result.getStatus());
    }

    @Test
    void testValidateProperty() {
        Long propertyId = 1L;
        Property mockProperty = createMockProperty();

        when(propertyRepository.findPropertyById(propertyId)).thenReturn(Optional.of(mockProperty));
        when(propertyRepository.setPropertyStatus(propertyId, PropertyStatus.VALIDATED.toString())).thenReturn(mockProperty);

        mockProperty.setStatus(PropertyStatus.VALIDATED);

        Property result = propertyService.validateProperty(propertyId);

        assertNotNull(result);
        assertEquals(PropertyStatus.VALIDATED, result.getStatus());
    }

    @Test
    void testInvalidateProperty() {
        Long propertyId = 1L;
        Property mockProperty = createMockProperty();

        mockProperty.setStatus(PropertyStatus.DELETED);

        when(propertyRepository.setPropertyStatus(propertyId, PropertyStatus.DELETED.toString())).thenReturn(mockProperty);

        Property result = propertyService.invalidateProperty(propertyId);
        assertNotNull(result);
        assertEquals(PropertyStatus.DELETED, result.getStatus());

    }
    @Test
    void testFindPropertiesByUserExcludingDeleted() {
        Long userId = 1L;
        List<Property> mockProperties = Arrays.asList(createMockProperty(), createMockProperty());
        when(propertyRepository.findByUserIdAndStatusNot(eq(userId), anyString())).thenReturn(mockProperties);

        List<Property> result = propertyService.findPropertiesByUserExcludingDeleted(userId);
        assertNotNull(result);
        assertFalse(result.isEmpty());

    }

    @Test
    void testFindPropertyById() {
        Long propertyId = 1L;
        Property mockProperty = createMockProperty();
        when(propertyRepository.findPropertyById(propertyId)).thenReturn(Optional.of(mockProperty));

        Optional<Property> result = propertyService.findPropertyById(propertyId);
        assertTrue(result.isPresent());
        assertEquals(propertyId, result.get().getId());
    }

    @Test
    void testFindAllProperties() {
        List<Property> mockProperties = Arrays.asList(createMockProperty(), createMockProperty());
        when(propertyRepository.findAllProperties()).thenReturn(mockProperties);

        List<Property> result = propertyService.findAllProperties();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindAllPendingValidation() {
        List<Property> mockProperties = Arrays.asList(createMockProperty(), createMockProperty());
        when(propertyRepository.findAllPendingValidation()).thenReturn(mockProperties);

        List<Property> result = propertyService.findAllPendingValidation();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindAllPropertiesInAuction() {
        List<Property> mockProperties = Arrays.asList(createMockProperty(), createMockProperty());
        when(propertyRepository.findAllInAuction()).thenReturn(mockProperties);

        List<Property> result = propertyService.findAllPropertiesInAuction();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindActivePropertiesByOwner() {
        Long userId = 1L;
        List<Property> mockProperties = Arrays.asList(createMockProperty(), createMockProperty());
        when(propertyRepository.findActivePropertiesByOwner(userId)).thenReturn(mockProperties);

        List<Property> result = propertyService.findActivePropertiesByOwner(userId);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

}
