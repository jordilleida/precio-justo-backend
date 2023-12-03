package edu.uod.tfg.property.domain.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    private Long id;

    private PropertyType type;
    private String description;
    private Integer rooms;
    private Integer baths;
    private Float surface;
    private PropertyStatus status;
    private String registryDocumentUrl;
    private String catastralReference;

    private Double latitude;
    private Double longitude;
    private Long userId;
    private String address;
    private List<PropertyImage> images;

    private PostalCode postalCode;
}
