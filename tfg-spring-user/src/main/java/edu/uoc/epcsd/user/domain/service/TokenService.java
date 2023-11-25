package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.UserSession;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
    String createToken(UserSession session);
    JSONObject solveToken(String token);
    UserDetails extractUserDetails(String jwtToken);
}
