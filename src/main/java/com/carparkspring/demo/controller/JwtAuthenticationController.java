package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.AppUser;
import com.carparkspring.demo.model.DTO.AuthenticationRequest;
import com.carparkspring.demo.service.JwtService;
import com.carparkspring.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userDetailsService;

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody AppUser userInfo) {
        userInfo.getAppUserData().setAppUser(userInfo);
        return userDetailsService.addUser(userInfo);
    }

//    @PostMapping("/addNewAdmin")
//    public String addNewAdmin(@RequestBody AppAdmin adminInfo) {
//        AppAdmin admin = new AppAdmin(adminInfo);
//        return "Logged in";
//    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    // Log out
    @GetMapping("/logout")
    public String logout() {
        return "Logged out";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to User Profile";
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmailId(), authenticationRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authenticationRequest.getEmailId());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}