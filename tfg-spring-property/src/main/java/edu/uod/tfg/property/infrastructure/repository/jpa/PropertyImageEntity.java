package edu.uod.tfg.property.infrastructure.repository.jpa;

import javax.persistence.*;

import edu.uod.tfg.property.domain.model.PropertyImage;
import lombok.*;

import javax.validation.constraints.NotNull;


@Table(name = "property_images")
@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyImageEntity implements DomainTranslatable<PropertyImage> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "image_url")
    @NotNull
    private String imageUrl;
    @Override
    public PropertyImage toDomain() {
        return PropertyImage.builder()
                .id(this.id)
                .imageUrl(this.imageUrl)
                .build();
    }
    public static PropertyImageEntity fromDomain(PropertyImage propertyImage) {
        PropertyImageEntity entity = new PropertyImageEntity();
        entity.setId(propertyImage.getId());
        entity.setImageUrl(propertyImage.getImageUrl());
        return entity;
    }
}
