package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.Role;
import edu.uoc.epcsd.user.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleRepositoryImpl implements RoleRepository {
    private final SpringDataRoleRepository roleJpaRepository;


    @Override
    public Optional<Role> findByName(String roleName) {
        return roleJpaRepository.findByName(roleName).map(RoleEntity::toDomain);
    }

    @Override
    public Optional<Role> getDefaultRole() {
        return roleJpaRepository.findById(1L).map(RoleEntity::toDomain);
    }
}
