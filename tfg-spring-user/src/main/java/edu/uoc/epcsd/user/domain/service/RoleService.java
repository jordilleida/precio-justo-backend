package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.Role;

import java.util.Optional;

public interface RoleService {
    Role getSellerRole();
    Role getDefaultRole();
}
