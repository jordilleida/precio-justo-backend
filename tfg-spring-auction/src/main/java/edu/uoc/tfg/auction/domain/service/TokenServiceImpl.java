package edu.uoc.tfg.auction.domain.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import edu.uoc.tfg.auction.infrastructure.security.CustomUserDetails;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class TokenServiceImpl implements TokenService {

    private final Algorithm algorithm;

    public TokenServiceImpl(@Value("${jwt.secret}") String secretKey) {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    @Override
    public JSONObject solveToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            JSONObject tokenData = new JSONObject();
            tokenData.put("email", jwt.getSubject());
            tokenData.put("expiresAt", jwt.getExpiresAt());
            tokenData.put("userId", jwt.getClaim("userId").asLong());
            tokenData.put("roles", jwt.getClaim("roles").asString());

            return tokenData;

        } catch (Exception e) {
            log.error("Error solving JWT: ", e);
            throw new RuntimeException("Token solving failed.");
        }
    }

    public UserDetails extractUserDetails(String jwtToken) {
        JSONObject tokenData = solveToken(jwtToken);
        String email = tokenData.getString("email");
        Long userId = tokenData.getLong("userId");
        String rolesString = tokenData.getString("roles");
        List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesString.split(", "))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim().toUpperCase()))
                .collect(Collectors.toList());

        return new CustomUserDetails(email, "", authorities, userId);
    }
}