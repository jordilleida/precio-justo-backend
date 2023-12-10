package edu.uoc.tfg.auction.infrastructure.security;

import java.util.Optional;

public interface UserService {
    Optional<CustomUserDetails> getAuthenticatedUser();
}
