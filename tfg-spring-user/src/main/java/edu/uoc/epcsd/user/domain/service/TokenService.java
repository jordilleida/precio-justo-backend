package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.UserSession;
import org.json.JSONObject;

import java.security.GeneralSecurityException;

public interface TokenService {
    String createToken(UserSession session);
    JSONObject solveToken(String token);
}
