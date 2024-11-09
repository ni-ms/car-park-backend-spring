package com.carparkspring.demo.service;

import com.carparkspring.demo.model.AppAdmin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class AdminService implements UserDetails {

    private AppAdmin appAdmin;

    public AdminService(AppAdmin appAdmin) {
        this.appAdmin = appAdmin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(appAdmin.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return appAdmin.getPassword();
    }

    @Override
    public String getUsername() {
        return appAdmin.getEmailId();
    }

    public String getFirstName() {
        return appAdmin.getFirstName();
    }

    public String getLastName() {
        return appAdmin.getLastName();
    }

    public String getMobileNumber() {
        return appAdmin.getMobileNumber();
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