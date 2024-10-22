package com.example.system.Security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.system.Entity.Employee;
import com.example.system.Repo.EmployeeRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepo employeeRepo;

    
    public CustomUserDetailsService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
        return new CustomUserDetails(employee);
    }
}

