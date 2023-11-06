package edu.uoc.epcsd.user.domain;

import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long id;
    private Rol name;
}
