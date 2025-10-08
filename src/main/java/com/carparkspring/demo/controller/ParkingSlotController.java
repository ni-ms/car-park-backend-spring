package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.ParkingSlot;
import com.carparkspring.demo.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parkingSlot")
@Tag(name = "Parking Slot", description = "Parking slot management APIs")
public class ParkingSlotController {

    @Autowired
    private ParkingService parkingService;

    @GetMapping("/getAllParkingSlots")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Get all parking slots", description = "Retrieve all parking slots in the system")
    public List<ParkingSlot> getAllParkingSlots() {
        return parkingService.getAllParkingSlots();
    }

    @GetMapping("/available/{parkingId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get available slots", description = "Get available parking slots for a specific parking facility")
    public List<ParkingSlot> getAvailableSlots(@PathVariable Long parkingId) {
        return parkingService.getAvailableSlots(parkingId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Get slot by ID", description = "Retrieve a specific parking slot by ID")
    public ParkingSlot getSlotById(@PathVariable Long id) {
        return parkingService.getSlotById(id);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Create parking slot", description = "Create a new parking slot")
    public ParkingSlot createSlot(@RequestBody ParkingSlot slot) {
        return parkingService.createSlot(slot);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Update parking slot", description = "Update an existing parking slot")
    public ParkingSlot updateSlot(@RequestBody ParkingSlot slot) {
        return parkingService.updateSlot(slot);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Update slot status", description = "Update the status of a parking slot")
    public ParkingSlot updateSlotStatus(@PathVariable Long id, @RequestParam ParkingSlot.SlotStatus status) {
        ParkingSlot slot = parkingService.getSlotById(id);
        slot.setSlotStatus(status);
        return parkingService.updateSlot(slot);
    }
}
