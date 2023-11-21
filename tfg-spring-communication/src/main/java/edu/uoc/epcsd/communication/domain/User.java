package edu.uoc.epcsd.communication.domain;

import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String fullName;

    private String password;

    private String email;

}
