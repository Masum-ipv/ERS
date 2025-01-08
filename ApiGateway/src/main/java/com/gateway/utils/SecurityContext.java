package com.gateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.List;

@Component
public class SecurityContext implements ServerSecurityContextRepository {
    private static final Logger logger = LoggerFactory.getLogger(SecurityContext.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    public String getUserNameFromToken(String token) {
        logger.debug("Getting username from JWT token: {}", token);
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    // Extract roles from JWT token
    public String extractRolesFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role").toString();
    }

    public Boolean validateToken(String authToken) {
        logger.debug("Validating JWT token: {}", authToken);
        try {
            // Perform JWT validation here
            Jwts.parserBuilder()
                    .setSigningKey(key()).build()
                    .parseClaimsJws(authToken);
            return true; // Return true if validation succeeds
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, org.springframework.security.core.context.SecurityContext context) {
        // Stateless authentication does not require saving the SecurityContext.
        return Mono.empty();
    }

    @Override
    public Mono<org.springframework.security.core.context.SecurityContext> load(ServerWebExchange exchange) {
        logger.debug("Token: {}", exchange.getRequest().getHeaders().getFirst("Authorization"));
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                // Validate the JWT token
                if (validateToken(token)) {
                    String username = getUserNameFromToken(token);
                    String role = extractRolesFromToken(token);
                    logger.debug("Role: {}", role);
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                    logger.debug("Authorities: {}", authorities.getFirst().getAuthority());

                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(username, token, authorities);
                    return Mono.just(new SecurityContextImpl(authentication));
                }
            } catch (Exception e) {
                // Log the error and return empty if token validation fails
                logger.error("Failed to load security context: {}", e.getMessage(), e);
                return Mono.empty();
            }
        }
        return Mono.empty();
    }
}
