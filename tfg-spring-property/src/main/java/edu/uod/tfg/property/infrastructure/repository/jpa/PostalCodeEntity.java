package edu.uod.tfg.property.infrastructure.repository.jpa;

import edu.uod.tfg.property.domain.model.Country;
import edu.uod.tfg.property.domain.model.PostalCode;
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
@Table(name = "postal_codes")
public class PostalCodeEntity implements DomainTranslatable<PostalCode> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code", unique = true)
    private String code;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;

    @Override
    public PostalCode toDomain() {
        return PostalCode.builder()
                .id(this.id)
                .code(this.code)
                .city(this.city.toDomain().getName())
                .build();
    }
    public static PostalCodeEntity fromDomain(PostalCode postalCode) {
        return PostalCodeEntity.builder()
                .id(postalCode.getId())
                .code(postalCode.getCode())
                .build();
    }

}