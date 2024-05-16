package com.carparkspring.demo.service;

import com.carparkspring.demo.model.AppAdmin;
import com.carparkspring.demo.model.AppUser;
import com.carparkspring.demo.repository.AdminRepository;
import com.carparkspring.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public MyUserDetailsService(UserRepository userRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.endsWith("@admin.com")) {
            return loadAppAdminByUsername(username);
        } else {
            return loadAppUserByUsername(username);
        }
    }

    private UserDetails loadAppUserByUsername(String username) {
        Optional<AppUser> user = userRepository.findByEmail(username);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.map(MyUserDetails::new).get();
    }

    private UserDetails loadAppAdminByUsername(String username) {
        Optional<AppAdmin> admin = adminRepository.findByEmail(username);
        admin.orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
        return admin.map(MyAdminDetails::new).get();
    }


}