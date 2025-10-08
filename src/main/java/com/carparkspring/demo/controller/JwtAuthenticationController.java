package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.AppUser;
import com.carparkspring.demo.model.WorkerData;
import com.carparkspring.demo.model.DTO.AuthenticationRequest;
import com.carparkspring.demo.model.DTO.AuthenticationResponse;
import com.carparkspring.demo.service.JwtService;
import com.carparkspring.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User registration and login")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    @Operation(summary = "Register as USER", description = "Public registration for regular users")
    public AuthenticationResponse register(@RequestBody AppUser userInfo) {

        userInfo.setRole("USER");

        if (userInfo.getAppUserData() != null) {
            userInfo.getAppUserData().setAppUser(userInfo);
        }

        userService.addUser(userInfo);
        String token = jwtService.generateToken(userInfo.getEmailId());

        return new AuthenticationResponse(token, userInfo.getEmailId(), "USER");
    }


    @PostMapping("/register/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Register admin", description = "Admin creates another admin")
    public AuthenticationResponse registerAdmin(@RequestBody AppUser adminInfo) {
        adminInfo.setRole("ADMIN");

        if (adminInfo.getAppUserData() != null) {
            adminInfo.getAppUserData().setAppUser(adminInfo);
        }

        userService.addUser(adminInfo);
        String token = jwtService.generateToken(adminInfo.getEmailId());

        return new AuthenticationResponse(token, adminInfo.getEmailId(), "ADMIN");
    }


    @PostMapping("/register/worker")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Register worker", description = "Admin creates a worker with work details")
    public AuthenticationResponse registerWorker(@RequestBody WorkerRegistrationRequest request) {

        AppUser workerUser = request.getUserInfo();
        workerUser.setRole("WORKER");

        if (workerUser.getAppUserData() != null) {
            workerUser.getAppUserData().setAppUser(workerUser);
        }


        WorkerData workerData = request.getWorkerData();
        workerData.setAppUser(workerUser);
        workerUser.setWorkerData(workerData);

        userService.addUser(workerUser);
        String token = jwtService.generateToken(workerUser.getEmailId());

        return new AuthenticationResponse(token, workerUser.getEmailId(), "WORKER");
    }


    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login for all user types")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmailId(),
                        authRequest.getPassword()
                )
        );

        if (!authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("Invalid credentials");
        }

        String token = jwtService.generateToken(authRequest.getEmailId());
        AppUser user = userService.getUserByEmail(authRequest.getEmailId());

        return new AuthenticationResponse(token, user.getEmailId(), user.getRole());
    }


    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get profile", description = "Get current user's profile")
    public AppUser getCurrentUserProfile(Authentication authentication) {
        return userService.getUserByEmail(authentication.getName());
    }


    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Change password", description = "Change current user's password")
    public String changePassword(
            Authentication authentication,
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        AppUser user = userService.getUserByEmail(authentication.getName());

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        userService.updatePassword(user.getId(), newPassword);
        return "Password changed successfully";
    }


    @GetMapping("/dashboard/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "User dashboard")
    public String userDashboard() {
        return "Welcome to User Dashboard";
    }

    @GetMapping("/dashboard/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Admin dashboard")
    public String adminDashboard() {
        return "Welcome to Admin Dashboard";
    }

    @GetMapping("/dashboard/worker")
    @PreAuthorize("hasAuthority('ROLE_WORKER')")
    @Operation(summary = "Worker dashboard")
    public String workerDashboard() {
        return "Welcome to Worker Dashboard";
    }


    @PostMapping("/logout")
    @Operation(summary = "Logout")
    public String logout() {
        return "Logged out successfully. Please discard your token.";
    }


    @Data
    public static class WorkerRegistrationRequest {
        private AppUser userInfo;
        private WorkerData workerData;
    }
}
