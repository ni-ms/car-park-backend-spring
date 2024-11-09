package com.carparkspring.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CarBookingData {
    // Booking Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String carModel;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String lpnumber;

    @ElementCollection
    private List<String> miscFacilities;

    @ManyToOne
    @JoinColumn(name = "slot_id", referencedColumnName = "id")
    private ParkingSlot parkingSlot;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser appUser;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private PaymentData paymentData;

    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    private WorkerData worker;

    @ElementCollection
    private List<String> tasks;
}
