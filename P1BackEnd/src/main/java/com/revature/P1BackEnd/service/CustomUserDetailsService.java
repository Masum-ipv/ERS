package com.revature.P1BackEnd.service;

import com.revature.P1BackEnd.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final EmployeeRepository authRepository;

    public CustomUserDetailsService(EmployeeRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authRepository.findByEmail(username) //As we are using Email for Authentication, otherwise findByUsername should be used
                .orElseThrow(() -> new UsernameNotFoundException("User not found with Email: " + username));
    }
}
