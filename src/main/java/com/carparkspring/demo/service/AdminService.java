package com.carparkspring.demo.service;

import com.carparkspring.demo.model.AppAdmin;
import com.carparkspring.demo.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public String addAdmin(AppAdmin admin) {
        log.info("Adding new admin: {}", admin.getUsername());

        if (adminRepository.existsByUsername(admin.getUsername())) {
            throw new RuntimeException("Admin already exists with username: " + admin.getUsername());
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        if (admin.getRoles() == null || admin.getRoles().isEmpty()) {
            admin.setRoles("ADMIN");
        }

        adminRepository.save(admin);
        log.info("Admin added successfully: {}", admin.getUsername());

        return "Admin added successfully";
    }

    public List<AppAdmin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public AppAdmin getAdminByUsername(String username) {
        return (AppAdmin) adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + username));
    }

    // Inner class for UserDetails implementation
    public static class AdminUserDetails implements UserDetails {
        private final AppAdmin appAdmin;

        public AdminUserDetails(AppAdmin appAdmin) {
            this.appAdmin = appAdmin;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Arrays.stream(appAdmin.getRoles().split(","))
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
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
}
