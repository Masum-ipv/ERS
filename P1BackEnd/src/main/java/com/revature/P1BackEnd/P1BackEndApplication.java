package com.revature.P1BackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
// TODO: Use Axio, Cokkies to send JWT
@SpringBootApplication
@EnableCaching // Enable redis caching
public class P1BackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(P1BackEndApplication.class, args);
	}

}
