package com.carparkspring.demo.service;

import com.carparkspring.demo.model.WorkerData;
import com.carparkspring.demo.model.ParkingSlot;
import com.carparkspring.demo.model.CarBookingData;
import com.carparkspring.demo.repository.WorkerDataRepository;
import com.carparkspring.demo.repository.ParkingSlotRepository;
import com.carparkspring.demo.repository.CarBookingDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WorkerService {

    @Autowired
    private WorkerDataRepository workerDataRepository;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    private CarBookingDataRepository carBookingDataRepository;

    public Optional<WorkerData> getWorkerById(Long id) {
        return workerDataRepository.findById(id);
    }

    public WorkerData saveWorker(WorkerData workerData) {
        return workerDataRepository.save(workerData);
    }

    public void deleteWorker(Long id) {
        workerDataRepository.deleteById(id);
    }

    public Map<String, Object> getWorkerDetails(Long workerId) {
        Optional<WorkerData> workerDataOptional = workerDataRepository.findById(workerId);
        if (!workerDataOptional.isPresent()) {
            return null;
        }

        WorkerData workerData = workerDataOptional.get();
        Map<String, Object> workerDetails = new HashMap<>();
        workerDetails.put("name", workerData.getName());
        workerDetails.put("position", workerData.getPosition());
        workerDetails.put("contactNumber", workerData.getContactNumber());

        List<ParkingSlot> parkingSlots = workerData.getParkingSlots();
        for (ParkingSlot slot : parkingSlots) {
            Map<String, Object> slotDetails = new HashMap<>();
            slotDetails.put("slotNumber", slot.getSlotNumber());
            slotDetails.put("spaceActive", slot.isSpaceActive());

            List<CarBookingData> carBookings = slot.getCarBookings();
            for (CarBookingData booking : carBookings) {
                Map<String, Object> bookingDetails = new HashMap<>();
                bookingDetails.put("customerName", booking.getAppUser().getUsername());
                bookingDetails.put("bookingTime", booking.getStartTime());
                slotDetails.put("carBookings", bookingDetails);
            }
            workerDetails.put("parkingSlot_" + slot.getSlotNumber(), slotDetails);
        }

        return workerDetails;
    }
}