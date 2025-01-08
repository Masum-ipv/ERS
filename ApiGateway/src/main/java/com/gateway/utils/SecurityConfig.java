package com.gateway.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final SecurityContext securityContext;

    public SecurityConfig(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {


        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Disable CSRF for stateless APIs
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.POST, "/employee/**").permitAll()  // Public access for /auth endpoints
                        .pathMatchers(HttpMethod.DELETE, "/employee/**").hasRole("MANAGER")
                        .anyExchange().authenticated()         // Require authentication for other routes
                )
                .securityContextRepository(securityContext)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((exchange, ex) -> {
                            logger.error("Unauthorized access: {}", ex.getMessage(), ex);

                            // Create ErrorResponse
                            ErrorResponse errorResponse = new ErrorResponse(
                                    "Failed", HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), null, null);

                            // Serialize ErrorResponse to JSON
                            ObjectMapper objectMapper = new ObjectMapper();
                            String jsonResponse = null;
                            try {
                                jsonResponse = objectMapper.writeValueAsString(errorResponse);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }

                            // Set response status and content type
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                            // Return response as JSON
                            return exchange.getResponse().writeWith(
                                    Mono.just(exchange.getResponse().bufferFactory().wrap(jsonResponse.getBytes()))
                            );
                        })
                        .accessDeniedHandler((exchange, ex) -> {
                            logger.error("Access denied: {}", ex.getMessage());

                            ErrorResponse errorResponse = new ErrorResponse(
                                    "Failed", HttpStatus.FORBIDDEN.value(), ex.getMessage(), null, null);

                            // Serialize ErrorResponse to JSON
                            ObjectMapper objectMapper = new ObjectMapper();
                            String jsonResponse = null;
                            try {
                                jsonResponse = objectMapper.writeValueAsString(errorResponse);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }

                            // Set response status and content type
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                            // Return response as JSON
                            return exchange.getResponse().writeWith(
                                    Mono.just(exchange.getResponse().bufferFactory().wrap(jsonResponse.getBytes()))
                            );
                        })
                )
                .build();
    }
}
