package edu.uod.tfg.property.domain.model;

import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    private Long id;

    private String name;
}
