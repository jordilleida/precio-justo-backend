package edu.uoc.tfg.gateway.security;

import edu.uoc.tfg.gateway.service.TokenService;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Log4j2
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private TokenService tokenService;
    private static final Set<String> EXEMPT_ROUTES = Set.of("/user/login");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        if (isPublicRoute(path)) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange);

        if (token == null || !validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    private boolean isPublicRoute(String path){
        return EXEMPT_ROUTES.contains(path);
    }

    private String extractToken(ServerWebExchange exchange) {
        List<String> headers = exchange.getRequest().getHeaders().get("Authorization");
        if (headers == null || headers.isEmpty()) {
            return null;
        }
        String authorizationHeader = headers.get(0);
        if (authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            JSONObject tokenData = tokenService.solveToken(token);
            Instant expiresAt = getInstantFromJson(tokenData,"expiresAt");

            if (expiresAt != null && expiresAt.isBefore(Instant.now())) {
                return false;
            }
            return true;

        } catch (RuntimeException e) {

            return false;
        }
    }
    public Instant getInstantFromJson(JSONObject jsonObject, String key) {
        try {
            String dateTimeString = jsonObject.optString(key);
            if (dateTimeString != null && !dateTimeString.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeString, formatter);
                return zonedDateTime.toInstant();
            }
        } catch (Exception e) {
            log.error("Error parsing date time: ", e);
            return null;
        }
        return null;
    }
    @Override
    public int getOrder() {
        return -1;
    }
}