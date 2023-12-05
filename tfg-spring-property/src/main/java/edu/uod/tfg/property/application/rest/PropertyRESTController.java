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
        //Muy importante recordar que es aqui donde se registra definitivamente
        // el nuevo propietario en el historyOwner y el anterior se pone como endDate de baja

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

        Optional<Property> existingProperty = propertyService.findPropertyByCatastralReference(newPropertyRequest.getCatastralReference());

        if (existingProperty.isPresent()) {

            PropertyStatus status = existingProperty.get().getStatus();

            if (!(status == PropertyStatus.SOLD || status == PropertyStatus.DELETED)) {
                // Utiliza el email extraído para enviar un email al propietario actual si es necesario
                // Lógica para enviar email y generar respuesta adecuada
                String responseMessage = "La propiedad no se puede agregar por estar actualmente activa," +
                                         "se ha enviado email al propietario actual, si la da de baja podrá insertarla.";

                return ResponseEntity.unprocessableEntity().body(responseMessage);
            }
        }

        // Convertir NewPropertyRequest a Property y establecer userId y email
        Property newProperty = PropertyMapper.convertToProperty(newPropertyRequest);
        newProperty.setUserId(userId);
        newProperty.setContact(email);

        // Crear la dirección de la propiedad
        PostalCode completeAddress = addressService.saveCompleteAddress(newPropertyRequest.getCountry(),
                                                                          newPropertyRequest.getRegion(),
                                                                            newPropertyRequest.getCity(),
                                                                              newPropertyRequest.getPostalCode());

       newProperty.setPostalCode(completeAddress);

        // Creo la propiedad a través del PropertyService
        Property createdProperty = propertyService.createProperty(newProperty);

        String responseMessage;
        if (createdProperty != null) {
            log.info("Property created with ID: " + createdProperty.getId());
            responseMessage = "Propiedad creada pendiente de validación.";
        } else {
            responseMessage = "Error al crear la propiedad.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }

        return ResponseEntity.ok(responseMessage);
    }



}