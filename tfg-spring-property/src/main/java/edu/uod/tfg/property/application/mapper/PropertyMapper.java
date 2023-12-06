package edu.uod.tfg.property.application.mapper;

import edu.uod.tfg.property.application.request.NewPropertyRequest;
import edu.uod.tfg.property.domain.model.Property;
import edu.uod.tfg.property.domain.model.PropertyImage;
import edu.uod.tfg.property.domain.model.PropertyType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PropertyMapper {
    public static Property convertToDomainEntity(NewPropertyRequest request, Long userId, String userEmail) {
        List<PropertyImage> images = new ArrayList<>();
        if (request.getImageUrls() != null) {
            images = request.getImageUrls().stream()
                    .map(url -> PropertyImage.builder().imageUrl(url).build())
                    .collect(Collectors.toList());
        }

        return Property.builder()
                .type(PropertyType.valueOf(request.getType().toUpperCase()))
                .description(request.getDescription())
                .rooms(request.getRooms())
                .baths(request.getBaths())
                .surface(request.getSurface())
                .registryDocumentUrl(request.getRegistryDocumentUrl())
                .catastralReference(request.getCatastralReference())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .address(request.getAddress())
                .images(images)
                .userId(userId)
                .contact(userEmail)
                // .status y .userId se establecerán en otra parte
                .build();
    }

}
