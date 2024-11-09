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
public class WorkerData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String position;
    private String contactNumber;

    @OneToMany(mappedBy = "worker")
    private List<ParkingSlot> parkingSlots;

    @OneToMany(mappedBy = "worker")
    private List<CarBookingData> carBookings;
}