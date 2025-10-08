package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.Parking;
import com.carparkspring.demo.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking")
@Tag(name = "Parking Facility", description = "Parking facility management APIs")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Get all parking facilities", description = "Retrieve all parking facilities")
    public List<Parking> getAllParkings() {
        return parkingService.getAllParkings();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Get parking by ID", description = "Retrieve a specific parking facility by ID")
    public Parking getParkingById(@PathVariable Long id) {
        return parkingService.getParkingById(id);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Create parking facility", description = "Create a new parking facility")
    public Parking createParking(@RequestBody Parking parking) {
        return parkingService.saveParking(parking);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Update parking facility", description = "Update an existing parking facility")
    public Parking updateParking(@RequestBody Parking parking) {
        return parkingService.saveParking(parking);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Delete parking facility", description = "Delete a parking facility")
    public String deleteParking(@PathVariable Long id) {
        parkingService.deleteParking(id);
        return "Parking facility deleted successfully";
    }
}
