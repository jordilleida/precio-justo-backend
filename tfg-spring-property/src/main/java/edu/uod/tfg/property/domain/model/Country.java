package edu.uod.tfg.property.domain.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    private Long id;

    private String name;
}
