package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.Role;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.UserSession;
import lombok.extern.log4j.Log4j2;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;


@Log4j2
@Service
public class TokenServiceImpl implements TokenService {

    private final Algorithm algorithm;

    public TokenServiceImpl(@Value("${jwt.secret}") String secretKey) {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    @Override
    public String createToken(UserSession session) {
        User user = session.getUser();
        Date now = new Date();
        Date expiryDate = java.util.Date.from(session.getExpireDate().atZone(ZoneId.systemDefault()).toInstant());


        String roles = user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.joining(", "));

        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .withClaim("id", user.getId())
                .withClaim("roles", roles)
                .sign(algorithm);
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

}
