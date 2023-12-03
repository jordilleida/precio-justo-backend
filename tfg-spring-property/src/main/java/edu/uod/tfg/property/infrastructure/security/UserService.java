package edu.uod.tfg.property.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {
    Optional<UserDetails> getAuthenticatedUser();
}
