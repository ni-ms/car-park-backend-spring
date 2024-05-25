package com.carparkspring.demo.service;

import com.carparkspring.demo.model.AppUser;
import com.carparkspring.demo.model.CarBookingData;
import com.carparkspring.demo.model.DTO.CarBookingRequest;
import com.carparkspring.demo.model.Parking;
import com.carparkspring.demo.repository.CarBookingDataRepository;
import com.carparkspring.demo.repository.ParkingRepository;
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
    public List<CarBookingData> getAllBookings() {
        return carBookingDataRepository.findAll();
    }

    public CarBookingData getBookingById(Long id) {
        return carBookingDataRepository.findById(id).orElse(null);
    }

    public CarBookingData saveBooking(CarBookingData carBookingData) {
        // Ensure parking is saved
        Parking parking = carBookingData.getParking();
        if (parking != null && parking.getId() == null) {
            parking = parkingRepository.save(parking);
            carBookingData.setParking(parking);
        }

        // Ensure appUser is saved
        AppUser appUser = carBookingData.getAppUser();
        if (appUser != null && appUser.getId() == null) {
            appUser = appUserRepository.save(appUser);
            carBookingData.setAppUser(appUser);
        }

        return carBookingDataRepository.save(carBookingData);
    }
    public CarBookingData bookCar(CarBookingRequest carBookingRequest) {
        // Retrieve Parking
        Parking parking = parkingRepository.findById(carBookingRequest.getParkingId())
                .orElseThrow(() -> new RuntimeException("Parking not found"));

        // Retrieve AppUser
        AppUser appUser = appUserRepository.findById(carBookingRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create CarBookingData
        CarBookingData carBookingData = new CarBookingData();
        carBookingData.setCarModel(carBookingRequest.getCarModel());
        carBookingData.setStartTime(carBookingRequest.getStartTime());
        carBookingData.setEndTime(carBookingRequest.getEndTime());
        carBookingData.setLpnumber(carBookingRequest.getLpnumber());
        carBookingData.setMiscFacilities(carBookingRequest.getMiscFacilities());
        carBookingData.setParking(parking);
        carBookingData.setAppUser(appUser);

        return saveBooking(carBookingData);
    }
    public void deleteBooking(Long id) {
        carBookingDataRepository.deleteById(id);
    }
}