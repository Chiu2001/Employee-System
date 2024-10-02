package com.example.system.Security;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.system.Entity.Employee;

public class CustomUserDetails implements UserDetails {
	
	private static final long serialVersionUID = 1L; // 設置 serialVersionUID

    @Autowired
    private Employee employee;

    public CustomUserDetails(Employee employee) {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String  role = employee.getRole();
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
