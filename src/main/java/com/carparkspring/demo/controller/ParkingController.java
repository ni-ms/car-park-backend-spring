package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.Parking;
import com.carparkspring.demo.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @GetMapping
    public List<Parking> getAllParkings() {
        return parkingService.getAllParkings();
    }

    @GetMapping("/{id}")
    public Parking getParkingById(@PathVariable Long id) {
        return parkingService.getParkingById(id);
    }

    @PostMapping
    public Parking saveParking(@RequestBody Parking parking) {
        return parkingService.saveParking(parking);
    }

    @DeleteMapping("/{id}")
    public void deleteParking(@PathVariable Long id) {
        parkingService.deleteParking(id);
    }
}