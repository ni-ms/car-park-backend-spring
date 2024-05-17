package com.carparkspring.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class AppAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String mobileNumber;

    private String username;
    private String password;
    private String emailId;
    private String roles;
}