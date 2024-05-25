package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.CarBookingData;
import com.carparkspring.demo.service.CarBookingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/carBookingData")
public class CarBookingDataController {

    @Autowired
    private CarBookingDataService carBookingDataService;

    @GetMapping
    public List<CarBookingData> getAllBookings() {
        return carBookingDataService.getAllBookings();
    }

    @GetMapping("/{id}")
    public CarBookingData getBookingById(@PathVariable Long id) {
        return carBookingDataService.getBookingById(id);
    }

    @PostMapping
    public CarBookingData saveBooking(@RequestBody CarBookingData carBookingData) {
        return carBookingDataService.saveBooking(carBookingData);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        carBookingDataService.deleteBooking(id);
    }
}