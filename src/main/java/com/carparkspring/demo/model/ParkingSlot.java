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
public class ParkingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int slotNumber; // Unique number for the slot within the parking area
    private boolean spaceActive; // Status of the slot (active or not)

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parking parking; // Association with Parking entity

    @OneToMany(mappedBy = "parkingSlot")
    private List<CarBookingData> carBookings; // Association with CarBookingData entity
}
