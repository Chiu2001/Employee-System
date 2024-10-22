package com.example.system.Security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.system.Entity.Employee;

public class CustomUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final Employee employee; // 定義為 final，這樣可以保證在構造時初始化

    public CustomUserDetails(Employee employee) {
        this.employee = employee; // 在構造函數中正確賦值
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = employee.getRole();
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

    // 新增方法來取得 employee 的 team
    public String getTeam() {
        return employee.getTeam() != null ? employee.getTeam().getName() : null;
    }
    

    // 新增方法來取得 employee 的 role
    public String getRole() {
        return employee.getRole();
    }
}


