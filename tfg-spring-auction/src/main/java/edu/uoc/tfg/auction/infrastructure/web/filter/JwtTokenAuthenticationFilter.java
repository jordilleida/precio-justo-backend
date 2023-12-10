package edu.uoc.tfg.auction.infrastructure.web.filter;

import edu.uoc.tfg.auction.domain.service.TokenService;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        log.info("Cual es el token {}", token);
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            UserDetails userDetails = tokenService.extractUserDetails(jwtToken);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        log.info("Request {}", request);
        log.info("Respuesta {}", response);
        filterChain.doFilter(request, response);
    }
}
