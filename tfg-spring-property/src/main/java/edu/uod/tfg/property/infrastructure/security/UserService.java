package edu.uod.tfg.property.infrastructure.security;

import java.util.Optional;

public interface UserService {
    Optional<CustomUserDetails> getAuthenticatedUser();
}
