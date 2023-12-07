package edu.uoc.epcsd.user.infrastructure.external;

import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    private Long id;

    private Long userId;
    private String contact;

}
