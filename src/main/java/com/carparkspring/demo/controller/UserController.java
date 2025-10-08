package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.AppUser;
import com.carparkspring.demo.model.AppUserData;
import com.carparkspring.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "User management APIs")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get all users", description = "Retrieve all users (Admin only)")
    public List<AppUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by ID")
    public Optional<AppUser> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/details")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get user details", description = "Get detailed user information")
    public Map<String, Object> getUserDetails(@PathVariable Long id) {
        return userService.getUserDetails(id);
    }

    @GetMapping("/{id}/data")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get user data", description = "Get user profile data")
    public AppUserData getUserData(@PathVariable Long id) {
        return userService.getUserData(id);
    }

    @GetMapping("/{id}/balance")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get account balance", description = "Get user's account balance")
    public Double getAccountBalance(@PathVariable Long id) {
        return userService.getAccountBalance(id);
    }

    @PutMapping("/{id}/username")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Update username", description = "Update user's username")
    public String updateUsername(@PathVariable Long id, @RequestParam String newUsername) {
        userService.updateUsername(id, newUsername);
        return "Username updated successfully";
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Update password", description = "Update user's password")
    public String updatePassword(@PathVariable Long id, @RequestParam String newPassword) {
        userService.updatePassword(id, newPassword);
        return "Password updated successfully";
    }

    @PutMapping("/{id}/data")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Update user data", description = "Update user profile data")
    public AppUserData updateUserData(@PathVariable Long id, @RequestBody AppUserData userData) {
        return userService.saveUserData(userData);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Delete user", description = "Delete a user (Admin only)")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }
}
