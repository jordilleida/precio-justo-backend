package edu.uod.tfg.property.domain.model;

import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostalCode {
    private Long id;

    private String code;

    private City city;
}

