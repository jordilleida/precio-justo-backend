package edu.uod.tfg.property.domain.model;

import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Region {
    private Long id;

    private String name;

    private String country;
}
