package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.Rol;
import edu.uoc.epcsd.user.domain.Role;
import lombok.*;

import javax.persistence.*;
@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class RoleEntity implements DomainTranslatable<Role>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Override

    public Role toDomain() {
        return Role.builder()
                .id(this.getId())
                .name(Rol.valueOf(this.getName()))
                .build();
    }
}
