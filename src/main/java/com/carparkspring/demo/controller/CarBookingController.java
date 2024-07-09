package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.CarBookingData;
import com.carparkspring.demo.model.DTO.CarBookingRequest;
import com.carparkspring.demo.service.CarBookingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/deleteBooking")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void deleteBooking(@RequestBody Long id) {
        carBookingDataService.deleteBooking(id);
    }

    @PutMapping("/updateBooking")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public CarBookingData updateBooking(@RequestBody CarBookingRequest carBookingRequest) {
        return carBookingDataService.saveBooking(carBookingRequest);
    }

    @GetMapping("/getBooking/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public CarBookingData getBooking(@PathVariable Long id) {
        return carBookingDataService.getBookingById(id);
    }

//    @GetMapping("/getAllBookings")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public List<CarBookingData> getAllBookings() {
//        return carBookingDataService.getAllBookings();
//    }




}
