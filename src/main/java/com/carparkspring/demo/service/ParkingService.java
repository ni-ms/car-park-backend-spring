package com.carparkspring.demo.service;

import com.carparkspring.demo.model.Parking;
import com.carparkspring.demo.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    public List<Parking> getAllParkings() {
        return parkingRepository.findAll();
    }

    public Parking getParkingById(Long id) {
        return parkingRepository.findById(id).orElse(null);
    }

    public Parking saveParking(Parking parking) {
        return parkingRepository.save(parking);
    }

    public void deleteParking(Long id) {
        parkingRepository.deleteById(id);
    }
}