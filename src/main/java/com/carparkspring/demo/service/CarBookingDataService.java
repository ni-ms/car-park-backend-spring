package com.carparkspring.demo.service;

import com.carparkspring.demo.model.CarBookingData;
import com.carparkspring.demo.repository.CarBookingDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarBookingDataService {

    @Autowired
    private CarBookingDataRepository carBookingDataRepository;

    public List<CarBookingData> getAllBookings() {
        return carBookingDataRepository.findAll();
    }

    public CarBookingData getBookingById(Long id) {
        return carBookingDataRepository.findById(id).orElse(null);
    }

    public CarBookingData saveBooking(CarBookingData carBookingData) {
        return carBookingDataRepository.save(carBookingData);
    }

    public void deleteBooking(Long id) {
        carBookingDataRepository.deleteById(id);
    }
}