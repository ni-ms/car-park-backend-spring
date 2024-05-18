package com.carparkspring.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AppUserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String residentialAddress;
    private String emailId;
    private String mobileNumber;
    private String carRegistrationNumber;
    private String carModel;
    private int frequency;
    private double discountPercentage;
    private double accountBalance;
    private boolean isWaitlisted;

    @OneToOne
    @JoinColumn(name = "appuser_id", referencedColumnName = "id")
    private AppUser appUser;
}