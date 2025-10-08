package com.carparkspring.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "app_user_data")
public class AppUserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String residentialAddress;

    @Column(unique = true)
    private String emailId;

    private String mobileNumber;
    private String carRegistrationNumber;
    private String carModel;

    @Column(columnDefinition = "int default 0")
    private int frequency;

    @Column(columnDefinition = "double default 0.0")
    private double discountPercentage;

    @Column(columnDefinition = "double default 0.0")
    private double accountBalance;

    @Column(columnDefinition = "boolean default false")
    private boolean isWaitlisted;

    @OneToOne
    @JoinColumn(name = "appuser_id", referencedColumnName = "id")
    @JsonIgnore
    private AppUser appUser;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;
}
