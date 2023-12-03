package edu.uod.tfg.property.infrastructure.repository.jpa;

import edu.uod.tfg.property.domain.model.Country;
import javax.persistence.*;

import lombok.*;

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "countries")
public class CountryEntity implements DomainTranslatable<Country> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Override
    public Country toDomain() {
        return Country.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }

    public static CountryEntity fromDomain(Country country) {
        return CountryEntity.builder()
                .id(country.getId())
                .name(country.getName())
                .build();
    }
}
