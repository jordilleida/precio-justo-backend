package edu.uod.tfg.property.domain.service;

import org.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
    JSONObject solveToken(String token);
    UserDetails extractUserDetails(String jwtToken);

}
