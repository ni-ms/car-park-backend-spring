package com.carparkspring.demo.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String emailId;
    private String role;
    private String message;

    
    public AuthenticationResponse(String token, String emailId, String role) {
        this.token = token;
        this.emailId = emailId;
        this.role = role;
        this.message = "Authentication successful";
    }
}
