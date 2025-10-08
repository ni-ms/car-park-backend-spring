package com.carparkspring.demo.service;

import com.carparkspring.demo.model.Parking;
import com.carparkspring.demo.model.ParkingSlot;
import com.carparkspring.demo.repository.ParkingRepository;
import com.carparkspring.demo.repository.ParkingSlotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Cacheable(value = "parkings")
    public List<Parking> getAllParkings() {
        log.debug("Fetching all parkings");
        return parkingRepository.findAll();
    }

    @Cacheable(value = "parking", key = "#id")
    public Parking getParkingById(Long id) {
        log.debug("Fetching parking by id: {}", id);
        return parkingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {"parkings", "parking"}, allEntries = true)
    public Parking saveParking(Parking parking) {
        log.info("Saving parking: {}", parking.getName());
        return parkingRepository.save(parking);
    }

    @Transactional
    @CacheEvict(value = {"parkings", "parking", "availableSlots"}, allEntries = true)
    public void deleteParking(Long id) {
        log.info("Deleting parking: {}", id);

        if (!parkingRepository.existsById(id)) {
            throw new RuntimeException("Parking not found with id: " + id);
        }

        parkingRepository.deleteById(id);
        log.info("Parking deleted successfully: {}", id);
    }

    @Cacheable(value = "availableSlots", key = "#parkingId")
    public List<ParkingSlot> getAvailableSlots(Long parkingId) {
        log.debug("Fetching available slots for parking: {}", parkingId);
        return parkingSlotRepository.findAvailableSlotsByParkingIdAndStatus(
                parkingId,
                ParkingSlot.SlotStatus.AVAILABLE
        );
    }

    public List<ParkingSlot> getAllParkingSlots() {
        log.debug("Fetching all parking slots");
        return parkingSlotRepository.findAll();
    }

    @Cacheable(value = "slot", key = "#id")
    public ParkingSlot getSlotById(Long id) {
        log.debug("Fetching slot by id: {}", id);
        return parkingSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking slot not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {"availableSlots", "slot"}, allEntries = true)
    public ParkingSlot createSlot(ParkingSlot slot) {
        log.info("Creating parking slot: {}", slot.getSlotNumber());
        return parkingSlotRepository.save(slot);
    }

    @Transactional
    @CacheEvict(value = {"availableSlots", "slot"}, allEntries = true)
    public ParkingSlot updateSlot(ParkingSlot slot) {
        log.info("Updating parking slot: {}", slot.getId());

        ParkingSlot existing = getSlotById(slot.getId());
        existing.setSlotNumber(slot.getSlotNumber());
        existing.setSpaceActive(slot.isSpaceActive());
        existing.setLocation(slot.getLocation());
        existing.setType(slot.getType());
        existing.setHourlyRate(slot.getHourlyRate());
        existing.setSlotStatus(slot.getSlotStatus());

        return parkingSlotRepository.save(existing);
    }
}
