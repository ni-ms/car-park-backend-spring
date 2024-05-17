package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.AppUser;
import com.carparkspring.demo.model.AuthenticationRequest;
import com.carparkspring.demo.service.JwtService;
import com.carparkspring.demo.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody AppUser userInfo) {
        return userDetailsService.addUser(userInfo);
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
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