package com.carparkspring.demo.service;

import com.carparkspring.demo.model.AppUser;
import com.carparkspring.demo.model.AppUserData;
import com.carparkspring.demo.repository.AppUserDataRepository;
import com.carparkspring.demo.repository.UserRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppUserDataRepository appUserDataRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Counter userRegistrationCounter;

    @Autowired
    public UserService(MeterRegistry meterRegistry) {
        this.userRegistrationCounter = Counter.builder("users.registered")
                .description("Total number of users registered")
                .register(meterRegistry);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        AppUser appUser = userRepository.findByUsername(username);
        if (appUser == null) {
            log.warn("User not found: {}", username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + appUser.getRole()));

        return new org.springframework.security.core.userdetails.User(
                appUser.getUsername(),
                appUser.getPassword(),
                authorities
        );
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public String addUser(AppUser appUser) {
        log.info("Adding new user: {}", appUser.getUsername());

        // Check if user already exists
        if (userRepository.findByUsername(appUser.getUsername()) != null) {
            log.warn("User already exists: {}", appUser.getUsername());
            throw new RuntimeException("User already exists with username: " + appUser.getUsername());
        }

        if (userRepository.findByEmailId(appUser.getEmailId()).isPresent()) {
            log.warn("Email already registered: {}", appUser.getEmailId());
            throw new RuntimeException("Email already registered: " + appUser.getEmailId());
        }

        // Encode password
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));

        // Set default role if not provided
        if (appUser.getRole() == null || appUser.getRole().isEmpty()) {
            appUser.setRole("USER");
        }

        // Save user and associated data
        if (appUser.getAppUserData() != null) {
            appUser.getAppUserData().setAppUser(appUser);
        }

        // Save worker data if present (for WORKER role)
        if (appUser.getWorkerData() != null) {
            appUser.getWorkerData().setAppUser(appUser);
        }

        userRepository.save(appUser);
        userRegistrationCounter.increment();

        log.info("User added successfully: {}", appUser.getUsername());
        return "User added successfully";
    }

    @Cacheable(value = "users", key = "#id")
    public Optional<AppUser> getUserById(Long id) {
        log.debug("Fetching user by id: {}", id);
        return userRepository.findById(id);
    }

    public AppUser getUserByEmail(String emailId) {
        log.debug("Fetching user by email: {}", emailId);
        return userRepository.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + emailId));
    }

    public List<AppUser> getAllUsers() {
        log.debug("Fetching all users");
        return userRepository.findAll();
    }

    public List<AppUser> getUsersByRole(String role) {
        log.debug("Fetching users by role: {}", role);
        return userRepository.findByRole(role);
    }

    @Transactional
    @CacheEvict(value = {"users", "userDetails"}, allEntries = true)
    public void deleteUser(Long id) {
        log.info("Deleting user: {}", id);

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully: {}", id);
    }

    @Cacheable(value = "userDetails", key = "#userId")
    public AppUserData getUserData(Long userId) {
        log.debug("Fetching user data for user: {}", userId);
        return appUserDataRepository.findByAppUserId(userId);
    }

    @Transactional
    @CacheEvict(value = "userDetails", key = "#appUserData.appUser.id")
    public AppUserData saveUserData(AppUserData appUserData) {
        log.info("Saving user data for user: {}", appUserData.getAppUser().getId());
        return appUserDataRepository.save(appUserData);
    }

    public double getAccountBalance(Long userId) {
        log.debug("Fetching account balance for user: {}", userId);
        AppUserData appUserData = appUserDataRepository.findByAppUserId(userId);
        return appUserData != null ? appUserData.getAccountBalance() : 0.0;
    }

    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void updateUsername(Long userId, String newUsername) {
        log.info("Updating username for user: {}", userId);

        AppUser existingUser = userRepository.findByUsername(newUsername);
        if (existingUser != null && !existingUser.getId().equals(userId)) {
            throw new RuntimeException("Username already taken: " + newUsername);
        }

        Optional<AppUser> appUserOptional = userRepository.findById(userId);
        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            appUser.setUsername(newUsername);
            userRepository.save(appUser);
            log.info("Username updated successfully for user: {}", userId);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void updatePassword(Long userId, String newPassword) {
        log.info("Updating password for user: {}", userId);

        Optional<AppUser> appUserOptional = userRepository.findById(userId);
        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            appUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(appUser);
            log.info("Password updated successfully for user: {}", userId);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    @Cacheable(value = "userDetails", key = "#userId")
    public Map<String, Object> getUserDetails(Long userId) {
        log.debug("Fetching user details for user: {}", userId);

        AppUserData appUserData = appUserDataRepository.findByAppUserId(userId);
        if (appUserData == null) {
            log.warn("User data not found for user: {}", userId);
            return null;
        }

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("residentialAddress", appUserData.getResidentialAddress());
        userDetails.put("emailId", appUserData.getEmailId());
        userDetails.put("mobileNumber", appUserData.getMobileNumber());
        userDetails.put("carRegistrationNumber", appUserData.getCarRegistrationNumber());
        userDetails.put("carModel", appUserData.getCarModel());
        userDetails.put("accountBalance", appUserData.getAccountBalance());
        userDetails.put("discountPercentage", appUserData.getDiscountPercentage());
        userDetails.put("isWaitlisted", appUserData.isWaitlisted());

        return userDetails;
    }
}
