package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.CarBookingData;
import com.carparkspring.demo.model.DTO.CarBookingRequest;
import com.carparkspring.demo.service.CarBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/carBooking")
@Tag(name = "Car Booking", description = "Car booking management APIs")
public class CarBookingController {

    @Autowired
    private CarBookingService carBookingDataService;

    @PostMapping("/addBooking")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Create booking", description = "Create a new car parking booking")
    public CarBookingData addBooking(@RequestBody CarBookingRequest carBookingRequest) {
        return carBookingDataService.saveBooking(carBookingRequest);
    }

    @DeleteMapping("/deleteBooking/{id}")  // Changed to proper REST
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Cancel booking", description = "Cancel an existing booking")
    public String deleteBooking(@PathVariable Long id) {
        carBookingDataService.deleteBooking(id);
        return "Booking cancelled successfully";
    }

    @PutMapping("/updateBooking")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Update booking", description = "Update an existing booking")
    public CarBookingData updateBooking(@RequestBody CarBookingRequest carBookingRequest) {
        return carBookingDataService.saveBooking(carBookingRequest);
    }

    @GetMapping("/getBooking/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Get booking by ID", description = "Retrieve a specific booking by ID")
    public CarBookingData getBooking(@PathVariable Long id) {
        return carBookingDataService.getBookingById(id);
    }

    @GetMapping("/getAllBookings")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get all bookings", description = "Retrieve all bookings (Admin only)")
    public List<CarBookingData> getAllBookings() {
        return carBookingDataService.getAllBookings();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get bookings by user", description = "Get all bookings for a specific user")
    public List<CarBookingData> getBookingsByUser(@PathVariable Long userId) {
        return carBookingDataService.getBookingsByUserId(userId);
    }

    @GetMapping("/{id}/cost")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Calculate booking cost", description = "Calculate the total cost for a booking")
    public BigDecimal calculateBookingCost(@PathVariable Long id) {
        return carBookingDataService.calculateBookingCost(id);
    }
}
