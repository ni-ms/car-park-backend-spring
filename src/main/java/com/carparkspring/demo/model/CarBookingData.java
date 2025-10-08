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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "car_bookings")
public class CarBookingData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String carModel;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String lpnumber;

    @ElementCollection
    @CollectionTable(name = "booking_facilities", joinColumns = @JoinColumn(name = "booking_id"))
    @Column(name = "facility")
    private List<String> miscFacilities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private ParkingSlot parkingSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private AppUser appUser;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PaymentData paymentData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    @JsonIgnore
    private WorkerData worker;

    @ElementCollection
    @CollectionTable(name = "car_booking_tasks", joinColumns = @JoinColumn(name = "car_booking_id"))
    @MapKeyColumn(name = "task_name")
    @Column(name = "task_status")
    private Map<String, String> tasks = new HashMap<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    public enum BookingStatus {
        PENDING, CONFIRMED, ACTIVE, COMPLETED, CANCELLED
    }
}
