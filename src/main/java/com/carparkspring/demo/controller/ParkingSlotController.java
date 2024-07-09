package com.carparkspring.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parkingSlot")
public class ParkingSlotController {

    //Get all parking slots
    @GetMapping("/getAllParkingSlots")
    public String getAllParkingSlots() {
        return "All parking slots";
    }
}
