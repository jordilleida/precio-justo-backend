package edu.uoc.epcsd.user.domain.repository;

import edu.uoc.epcsd.user.domain.Role;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> getSellerRole();
    Optional<Role> getDefaultRole();
}
