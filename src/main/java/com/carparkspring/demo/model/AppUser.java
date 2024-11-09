package com.carparkspring.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String emailId;
    private String role;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private AppUserData appUserData;

    @OneToMany(mappedBy = "appUser")
    private List<CarBookingData> carBookings;
}
