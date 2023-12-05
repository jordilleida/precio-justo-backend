package edu.uod.tfg.property.infrastructure.repository.jpa;

import edu.uod.tfg.property.domain.model.Property;
import edu.uod.tfg.property.domain.model.PropertyStatus;
import edu.uod.tfg.property.domain.model.PropertyType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "properties")
public class PropertyEntity implements DomainTranslatable<Property>  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String description;
    private Integer rooms;
    private Integer baths;
    private Float surface;
    private String status;
    @Column(name = "registry_document_url")
    private String registryDocumentUrl;

    @NotNull
    @Column(name = "catastral_reference", unique = true, nullable = false)
    private String catastralReference;

    @Column(name = "user_id")
    @NotNull
    private Long userId;

    private String contact;
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "postal_code_id")
    private PostalCodeEntity postalCode;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "property_id")
    private List<PropertyImageEntity> images;
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Override
    public Property toDomain() {

        return Property.builder()
                .id(this.id)
                .type(PropertyType.valueOf(this.type))
                .description(this.description)
                .rooms(this.rooms)
                .baths(this.baths)
                .surface(this.surface)
                .status(PropertyStatus.valueOf(this.status))
                .registryDocumentUrl(this.registryDocumentUrl)
                .catastralReference(this.catastralReference)
                .userId(this.userId)
                .contact(this.contact)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .postalCode(this.postalCode.toDomain())
                .images(this.images != null ? this.images.stream()
                                                         .map(PropertyImageEntity::toDomain)
                                                         .collect(Collectors.toList())
                                            : null)
                .build();
    }
    public static PropertyEntity fromDomain(Property property) {

        List<PropertyImageEntity> images = null;
        if (property.getImages() != null) {
            images = property.getImages().stream()
                    .map(PropertyImageEntity::fromDomain)
                    .collect(Collectors.toList());
        }

        return PropertyEntity.builder()
                .id(property.getId())
                .type(property.getType().toString())
                .description(property.getDescription())
                .rooms(property.getRooms())
                .baths(property.getBaths())
                .surface(property.getSurface())
                .status(property.getStatus().toString())
                .registryDocumentUrl(property.getRegistryDocumentUrl())
                .catastralReference(property.getCatastralReference())
                .userId(property.getUserId())
                .contact(property.getContact())
                .address(property.getAddress())
                .latitude(property.getLatitude())
                .longitude(property.getLongitude())
                .postalCode( PostalCodeEntity.fromDomain(property.getPostalCode()))
                .images(images)
                .build();
    }
}
