package com.carparkspring.demo.service;

import com.carparkspring.demo.model.AppUser;
import com.carparkspring.demo.model.CarBookingData;
import com.carparkspring.demo.model.DTO.CarBookingRequest;
import com.carparkspring.demo.model.Parking;
import com.carparkspring.demo.model.ParkingSlot;
import com.carparkspring.demo.repository.CarBookingDataRepository;
import com.carparkspring.demo.repository.ParkingRepository;
import com.carparkspring.demo.repository.ParkingSlotRepository;
import com.carparkspring.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarBookingDataService {

    @Autowired
    private CarBookingDataRepository carBookingDataRepository;
    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private UserRepository appUserRepository;
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    public List<CarBookingData> getAllBookings() {
        return carBookingDataRepository.findAll();
    }

    public CarBookingData getBookingById(Long id) {
        return carBookingDataRepository.findById(id).orElse(null);
    }

    public CarBookingData saveBooking(CarBookingRequest carBookingRequest) {
        ParkingSlot parkingSlot = parkingSlotRepository.findById(carBookingRequest.getParkingId()).orElse(null);
        AppUser appUser = appUserRepository.findById(carBookingRequest.getUserId()).orElse(null);

        CarBookingData carBookingData = new CarBookingData();
        carBookingData.setCarModel(carBookingRequest.getCarModel());
        carBookingData.setStartTime(carBookingRequest.getStartTime());
        carBookingData.setEndTime(carBookingRequest.getEndTime());
        carBookingData.setLpnumber(carBookingRequest.getLpnumber());
        carBookingData.setMiscFacilities(carBookingRequest.getMiscFacilities());
        carBookingData.setParkingSlot(parkingSlot);
        carBookingData.setAppUser(appUser);

        return carBookingDataRepository.save(carBookingData);
    }

    public void deleteBooking(Long id) {
        carBookingDataRepository.deleteById(id);
    }
}