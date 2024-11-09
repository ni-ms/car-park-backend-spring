package com.carparkspring.demo.service;

import com.carparkspring.demo.model.AppUser;
import com.carparkspring.demo.model.AppUserData;
import com.carparkspring.demo.repository.AppUserDataRepository;
import com.carparkspring.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppUserDataRepository appUserDataRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + appUser.getRole()));

        return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), authorities);
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<AppUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public AppUser saveUser(AppUser appUser) {
        return userRepository.save(appUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public AppUserData getUserData(Long userId) {
        return appUserDataRepository.findByAppUserId(userId);
    }

    public AppUserData saveUserData(AppUserData appUserData) {
        return appUserDataRepository.save(appUserData);
    }

    public double getAccountBalance(Long userId) {
        AppUserData appUserData = appUserDataRepository.findByAppUserId(userId);
        return appUserData != null ? appUserData.getAccountBalance() : 0.0;
    }

    public void updateUsername(Long userId, String newUsername) {
        Optional<AppUser> appUserOptional = userRepository.findById(userId);
        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            appUser.setUsername(newUsername);
            userRepository.save(appUser);
        }
    }

    public void updatePassword(Long userId, String newPassword) {
        Optional<AppUser> appUserOptional = userRepository.findById(userId);
        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            appUser.setPassword(newPassword);
            userRepository.save(appUser);
        }
    }
}