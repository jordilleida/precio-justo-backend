package edu.uoc.epcsd.user.infrastructure.web.filter;

import edu.uoc.epcsd.user.domain.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService jwtDecoderService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Accedo a DOFILTERINTERNAL ???");

        String token = request.getHeader("Authorization");
        log.info("TOKEN ??? {}", token);

        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            UserDetails userDetails = jwtDecoderService.extractUserDetails(jwtToken);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            log.info("Accedo a filtro {}", userDetails.getAuthorities().toString());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}