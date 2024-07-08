package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.CarBookingData;
import com.carparkspring.demo.model.DTO.CarBookingRequest;
import com.carparkspring.demo.service.CarBookingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carBooking")
public class CarBookingController {
    @Autowired
    private CarBookingDataService carBookingDataService;

    @PostMapping("/addBooking")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public CarBookingData addBooking(@RequestBody CarBookingRequest carBookingRequest) {
        return carBookingDataService.saveBooking(carBookingRequest);
    }
}
