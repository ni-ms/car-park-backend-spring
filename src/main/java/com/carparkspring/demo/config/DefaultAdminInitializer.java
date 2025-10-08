package com.carparkspring.demo.config;

import com.carparkspring.demo.model.AppUser;
import com.carparkspring.demo.model.AppUserData;
import com.carparkspring.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;



@Component
@Slf4j
public class DefaultAdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminProperties adminProperties;

    @Override
    public void run(String... args) throws Exception {
        // Only create default admin if enabled in properties
        if (!adminProperties.isEnabled()) {
            log.info("Default admin creation is disabled");
            return;
        }

        // Check if default admin already exists
        if (userRepository.existsByUsername(adminProperties.getUsername())) {
            log.info("Default admin user '{}' already exists, skipping creation",
                    adminProperties.getUsername());
            return;
        }

        // Check if email already exists
        if (userRepository.existsByEmailId(adminProperties.getEmail())) {
            log.warn("Email '{}' already exists, skipping default admin creation",
                    adminProperties.getEmail());
            return;
        }

        // Create default admin user
        AppUser adminUser = new AppUser();
        adminUser.setUsername(adminProperties.getUsername());
        adminUser.setPassword(passwordEncoder.encode(adminProperties.getPassword()));
        adminUser.setEmailId(adminProperties.getEmail());
        adminUser.setRole("ADMIN");

        // Create admin user data
        AppUserData adminData = new AppUserData();
        adminData.setFirstName(adminProperties.getFirstName());
        adminData.setLastName(adminProperties.getLastName());
        adminData.setUserName(adminProperties.getUsername());
        adminData.setEmailId(adminProperties.getEmail());
        adminData.setAppUser(adminUser);

        adminUser.setAppUserData(adminData);

        // Save admin user
        userRepository.save(adminUser);

        log.info("===========================================");
        log.info("DEFAULT ADMIN USER CREATED SUCCESSFULLY");
        log.info("===========================================");
        log.info("Username: {}", adminProperties.getUsername());
        log.info("Email:    {}", adminProperties.getEmail());
        log.info("Password: {}", adminProperties.getPassword());
        log.info("===========================================");
        log.warn("IMPORTANT: Please change the default admin password after first login!");
        log.info("===========================================");
    }
}
