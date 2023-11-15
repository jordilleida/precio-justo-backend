package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.Rol;
import edu.uoc.epcsd.user.domain.Role;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.infrastructure.mapper.UserRoleMapper;
import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity implements DomainTranslatable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;
    
    @Column(name = "roles")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;

    @Column(name = "disabled")
    private Boolean disabled;
    public static UserEntity fromDomain(User user) {

        if (user == null) return null;

        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(UserRoleMapper.mapRolesToEntities(user.getRoles()))
                .disabled(user.getDisabled())
                .build();
    }

    @Override
    public User toDomain() {

        return User.builder()
                .id(this.id)
                .name(this.name)
                .surname(this.surname)
                .email(this.email)
                .password(this.password)
                .roles(UserRoleMapper.mapEntitiesToRoles(this.roles))
                .disabled(this.disabled)
                .build();
    }
}
