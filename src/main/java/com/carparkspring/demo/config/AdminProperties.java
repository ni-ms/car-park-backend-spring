package com.carparkspring.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.admin.default")
@Data
public class AdminProperties {
    private boolean enabled;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
}
