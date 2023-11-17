package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.Rol;
import edu.uoc.epcsd.user.domain.Role;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.infrastructure.mapper.UserRoleMapper;
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

    @Enumerated(EnumType.STRING)
    private Rol name;

    @Override

    public Role toDomain() {
        return Role.builder()
                .id(this.getId())
                .name(this.getName())
                .build();
    }
}
