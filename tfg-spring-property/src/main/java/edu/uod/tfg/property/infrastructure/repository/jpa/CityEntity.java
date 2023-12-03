package edu.uod.tfg.property.infrastructure.repository.jpa;

import edu.uod.tfg.property.domain.model.City;
import edu.uod.tfg.property.domain.model.Country;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cities")
public class CityEntity implements DomainTranslatable<City> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private RegionEntity region;

    @Override
    public City toDomain() {
        return City.builder()
                .id(this.id)
                .name(this.name)
                .region(this.region.toDomain().getName())
                .build();
    }
    public static CityEntity fromDomain(City city) {
        return CityEntity.builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }
}
