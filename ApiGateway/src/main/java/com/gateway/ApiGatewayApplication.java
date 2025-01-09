package com.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

/*
1. Add OAuth2 integration.
2. Add Refresh Token
3. Circuit Breaker (Hystrix).
4. Http only Cookies
 */

// https://www.geeksforgeeks.org/reactive-jwt-authentication-using-spring-webflux/
// https://medium.com/@ashraful.ph041/secure-rest-api-spring-webflux-spring-security-jwt-redis-b7e87045f8a9

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
