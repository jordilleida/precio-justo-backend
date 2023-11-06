package edu.uoc.epcsd.user.domain;

import lombok.*;

import java.util.Set;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private String password;
    
    private Set<Role> roles;

    private Boolean disabled;
}
