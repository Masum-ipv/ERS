package com.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

/*
 OAuth2 integration.
1. Add Role Based Authorization
2. Add Refresh Token
3. Circuit Breaker (Hystrix).
Lazy vs. Eager loading, Caching strategies.
5. Spring boot with AWS
 */

// https://www.geeksforgeeks.org/reactive-jwt-authentication-using-spring-webflux/
// https://medium.com/@ashraful.ph041/secure-rest-api-spring-webflux-spring-security-jwt-redis-b7e87045f8a9

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
