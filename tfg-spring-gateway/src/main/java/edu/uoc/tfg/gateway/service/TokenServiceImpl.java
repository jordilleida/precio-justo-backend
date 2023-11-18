package edu.uoc.tfg.gateway.service;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
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

            throw new RuntimeException("Token solving failed.");
        }
    }

}