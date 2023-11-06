package edu.uoc.epcsd.user.infrastructure.mapper;
import edu.uoc.epcsd.user.domain.Role;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserRoleMapper {

    public static Set<RoleEntity> mapRolesToEntities(Set<Role> roleDTOs) {
        return roleDTOs.stream()
                .map(roleDTO -> new RoleEntity(roleDTO.getId(), roleDTO.getName()))
                .collect(Collectors.toSet());
    }

    public static Set<Role> mapEntitiesToRoles(Set<RoleEntity> roleEntities) {
        return roleEntities.stream()
                .map(roleEntity -> new Role(roleEntity.getId(), roleEntity.getName()))
                .collect(Collectors.toSet());
    }
}
