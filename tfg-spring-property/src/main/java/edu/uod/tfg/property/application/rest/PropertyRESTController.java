package edu.uod.tfg.property.application.rest;


import edu.uod.tfg.property.application.mapper.PropertyMapper;
import edu.uod.tfg.property.application.request.NewPropertyRequest;
import edu.uod.tfg.property.domain.model.OwnerHistory;
import edu.uod.tfg.property.domain.model.PostalCode;
import edu.uod.tfg.property.domain.model.Property;
import edu.uod.tfg.property.domain.model.PropertyStatus;
import edu.uod.tfg.property.domain.service.AddressService;
import edu.uod.tfg.property.domain.service.PropertyService;
import edu.uod.tfg.property.infrastructure.security.CustomUserDetails;
import edu.uod.tfg.property.infrastructure.security.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@CrossOrigin
public class PropertyRESTController {

    private final PropertyService propertyService;
    private final AddressService addressService;
    private final UserService userService;

    @GetMapping("/properties")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Property>> findProperties() {
        List<Property> properties = propertyService.findAllProperties();
        if (properties.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(properties);
        }
    }
    @GetMapping("/properties/{userId}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<List<Property>> getUserProperties(@PathVariable Long userId) {

        Optional<CustomUserDetails> userDetails = userService.getAuthenticatedUser();
        if (!userDetails.isPresent())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Long authenticatedUserId = userDetails.get().getUserId();

        if(authenticatedUserId != userId)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        List<Property> properties = propertyService.findPropertiesByUserExcludingDeleted(userId);
        if (properties.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(properties);
        }
    }
    @GetMapping("/owners")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<OwnerHistory>> findOwners() {
        List<OwnerHistory> owners = propertyService.findAllOwnersHistory();
        if (owners.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(owners);
        }
    }
    @GetMapping("/auction")

    public ResponseEntity<List<Property>> getPropertiesInAuction() {
        List<Property> properties = propertyService.findAllPropertiesInAuction();
        if (properties.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(properties);
        }
    }

    @GetMapping("/pending-validation")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Property>> getPropertiesPendingValidation() {
        List<Property> properties = propertyService.findAllPendingValidation();
        if (properties.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(properties);
        }
    }

    @PutMapping("/validate/{propertyId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Property> validateProperty(@PathVariable Long propertyId) {

        Property property = propertyService.validateProperty(propertyId);
        if (property != null) {
            return ResponseEntity.ok(property);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/invalidate/{propertyId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Property> invalidateProperty(@PathVariable Long propertyId) {
        Property property = propertyService.invalidateProperty(propertyId);
        if (property != null) {
            return ResponseEntity.ok(property);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/delete/{propertyId}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<Property> deleteProperty(@PathVariable Long propertyId) {

        Optional<CustomUserDetails> userDetails = userService.getAuthenticatedUser();
        if (!userDetails.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long authenticatedUserId = userDetails.get().getUserId();

        Property property = propertyService.deleteProperty(propertyId, authenticatedUserId);

        if (property != null) {
            return ResponseEntity.ok(property);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/create")
    public ResponseEntity<String> createProperty(@Valid @RequestBody NewPropertyRequest newPropertyRequest) {

        // De momento no envio imagenes para probar el objeto en si
        // , @RequestParam("images") List<MultipartFile> images

        Optional<CustomUserDetails> userDetails = userService.getAuthenticatedUser();
        if (!userDetails.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado.");
        }

        Long userId = userDetails.get().getUserId();
        String email = userDetails.get().getUsername();

        Optional<Property> existingPropertyOpt = propertyService.findPropertyByCatastralReference(newPropertyRequest.getCatastralReference());

        if (existingPropertyOpt.isPresent()) {

            Property existingProperty = existingPropertyOpt.get();

            PropertyStatus status = existingProperty.getStatus();

            if (!(status == PropertyStatus.SOLD || status == PropertyStatus.DELETED)) {

                // Utilizo el email de la petición para enviar un email al propietario actual si es necesario
                propertyService.sendChangePropertyRequest(existingProperty, email);

                String responseMessage = "La propiedad no se puede agregar por estar actualmente activa," +
                                         "se ha enviado email al propietario actual, si la da de baja podrás crearla.";

                return ResponseEntity.unprocessableEntity().body(responseMessage);
            }
        }

        // Convertir NewPropertyRequest a Property y establecer userId y email
        Property newProperty = PropertyMapper.convertToDomainEntity(newPropertyRequest, userId, email);

        // Crear la dirección de la propiedad
        PostalCode completeAddress = addressService.saveCompleteAddress(newPropertyRequest.getCountry(),
                                                                          newPropertyRequest.getRegion(),
                                                                            newPropertyRequest.getCity(),
                                                                              newPropertyRequest.getPostalCode());

        newProperty.setPostalCode(completeAddress);

        // Creo la propiedad a través del PropertyService
        Property createdProperty = propertyService.createProperty(newProperty);

        if(createdProperty == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la propiedad.");
        }

        log.info("Property created with ID: " + createdProperty.getId());

        return ResponseEntity.ok("Propiedad creada pendiente de validación.");
    }
}
