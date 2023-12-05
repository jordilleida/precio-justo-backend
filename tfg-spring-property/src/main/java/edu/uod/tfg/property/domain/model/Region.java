package edu.uod.tfg.property.domain.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Region {
    private Long id;

    private String name;

    private Country country;
}
