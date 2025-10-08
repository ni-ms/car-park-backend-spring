package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.AppAdmin;
import com.carparkspring.demo.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Admin management APIs")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Create admin", description = "Create a new admin user")
    public String createAdmin(@RequestBody AppAdmin admin) {
        return adminService.addAdmin(admin);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get all admins", description = "Retrieve all admin users")
    public List<AppAdmin> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get admin by username", description = "Retrieve a specific admin by username")
    public AppAdmin getAdminByUsername(@PathVariable String username) {
        return adminService.getAdminByUsername(username);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get admin profile", description = "Get current admin profile")
    public String getAdminProfile() {
        return "Welcome to Admin Dashboard";
    }
}
