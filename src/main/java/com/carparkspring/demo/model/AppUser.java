package com.carparkspring.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "app_users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String emailId;

    @Column(nullable = false)
    private String role;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AppUserData appUserData;

    // ADD THIS: Link to WorkerData (only populated if role = WORKER)
    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private WorkerData workerData;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<CarBookingData> carBookings;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AdminMetadata adminMetadata;


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
