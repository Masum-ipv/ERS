package com.revature.P1BackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

// TODO: Message broker, JWT token and Role Based Access Control (RBAC), and
//  Logout functionality with JWT token blacklisting
// TODO: Add Readme file mentioning Backend and Frontend technologies used, and how to run the application(e.g. lombok: Used to store log file )
// TODO: Use Axio, Cokkies to send JWT
@SpringBootApplication
@EnableCaching // Enable redis caching
public class P1BackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(P1BackEndApplication.class, args);
	}

}
