package edu.uod.tfg.property.infrastructure.repository.jpa;

import edu.uod.tfg.property.domain.model.Country;
import edu.uod.tfg.property.domain.model.Region;
import javax.persistence.*;

import lombok.*;

import javax.validation.constraints.NotNull;

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "regions")
public class RegionEntity implements DomainTranslatable<Region> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", unique = true)
    private String name;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity country;

    @Override
    public Region toDomain() {
        return Region.builder()
                .id(this.id)
                .name(this.name)
                .country(this.country.toDomain().getName())
                .build();
    }
    public static RegionEntity fromDomain(Region region) {
        return RegionEntity.builder()
                .id(region.getId())
                .name(region.getName())
                .build();
    }
}
