package com.carparkspring.demo.service;

import com.carparkspring.demo.model.AppUser;
import com.carparkspring.demo.model.CarBookingData;
import com.carparkspring.demo.model.DTO.CarBookingRequest;
import com.carparkspring.demo.model.ParkingSlot;
import com.carparkspring.demo.repository.CarBookingDataRepository;
import com.carparkspring.demo.repository.ParkingRepository;
import com.carparkspring.demo.repository.ParkingSlotRepository;
import com.carparkspring.demo.repository.UserRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class CarBookingService {

    @Autowired
    private CarBookingDataRepository carBookingDataRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private UserRepository appUserRepository;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired(required = false)
    private SimpMessagingTemplate messagingTemplate;

    private final Counter bookingCounter;
    private final Counter bookingCancellationCounter;
    private final Timer bookingTimer;

    public CarBookingService(MeterRegistry meterRegistry) {
        this.bookingCounter = Counter.builder("bookings.created")
                .description("Total number of bookings created")
                .register(meterRegistry);

        this.bookingCancellationCounter = Counter.builder("bookings.cancelled")
                .description("Total number of bookings cancelled")
                .register(meterRegistry);

        this.bookingTimer = Timer.builder("bookings.processing.time")
                .description("Time taken to process bookings")
                .register(meterRegistry);
    }

    public List<CarBookingData> getAllBookings() {
        log.debug("Fetching all bookings");
        return carBookingDataRepository.findAll();
    }

    @Cacheable(value = "booking", key = "#id")
    public CarBookingData getBookingById(Long id) {
        log.debug("Fetching booking by id: {}", id);
        return carBookingDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    public List<CarBookingData> getBookingsByUserId(Long userId) {
        log.debug("Fetching bookings for user: {}", userId);
        return carBookingDataRepository.findByAppUserId(userId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Retryable(
            retryFor = {OptimisticLockException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 100, multiplier = 2)
    )
    @CacheEvict(value = {"availableSlots", "booking"}, allEntries = true)
    public CarBookingData saveBooking(CarBookingRequest carBookingRequest) {
        return bookingTimer.record(() -> {
            log.info("Creating booking - User: {}, Slot: {}",
                    carBookingRequest.getUserId(), carBookingRequest.getParkingId());

            
            if (carBookingRequest.getEndTime().isBefore(carBookingRequest.getStartTime())) {
                throw new RuntimeException("End time must be after start time");
            }

            if (carBookingRequest.getStartTime().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Start time cannot be in the past");
            }

            
            ParkingSlot parkingSlot = (ParkingSlot) parkingSlotRepository.findByIdWithLock(carBookingRequest.getParkingId())
                    .orElseThrow(() -> new RuntimeException("Parking slot not found"));

            AppUser appUser = appUserRepository.findById(carBookingRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            
            if (!parkingSlot.isSpaceActive() ||
                    parkingSlot.getSlotStatus() == ParkingSlot.SlotStatus.MAINTENANCE) {
                throw new RuntimeException("Parking slot is not available");
            }

            
            List<CarBookingData> conflicts = carBookingDataRepository.findConflictingBookings(
                    carBookingRequest.getParkingId(),
                    carBookingRequest.getStartTime(),
                    carBookingRequest.getEndTime()
            );

            if (!conflicts.isEmpty()) {
                log.warn("Slot {} already booked for requested time", carBookingRequest.getParkingId());
                throw new RuntimeException("Slot is already booked for the requested time");
            }

            
            CarBookingData carBookingData = new CarBookingData();
            carBookingData.setCarModel(carBookingRequest.getCarModel());
            carBookingData.setStartTime(carBookingRequest.getStartTime());
            carBookingData.setEndTime(carBookingRequest.getEndTime());
            carBookingData.setLpnumber(carBookingRequest.getLpnumber());
            carBookingData.setMiscFacilities(carBookingRequest.getMiscFacilities());
            carBookingData.setParkingSlot(parkingSlot);
            carBookingData.setAppUser(appUser);
            carBookingData.setStatus(CarBookingData.BookingStatus.CONFIRMED);

            
            parkingSlot.setSlotStatus(ParkingSlot.SlotStatus.RESERVED);
            parkingSlotRepository.save(parkingSlot);

            CarBookingData savedBooking = carBookingDataRepository.save(carBookingData);

            
            if (messagingTemplate != null) {
                messagingTemplate.convertAndSend("/topic/bookings", savedBooking);
                messagingTemplate.convertAndSend("/topic/occupancy/" + parkingSlot.getParking().getId(),
                        getOccupancyUpdate(parkingSlot.getParking().getId()));
            }

            bookingCounter.increment();
            log.info("Booking created successfully with id: {}", savedBooking.getId());

            return savedBooking;
        });
    }

    @Transactional
    @CacheEvict(value = {"availableSlots", "booking"}, allEntries = true)
    public void deleteBooking(Long id) {
        log.info("Cancelling booking: {}", id);

        CarBookingData booking = carBookingDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(CarBookingData.BookingStatus.CANCELLED);

        ParkingSlot slot = booking.getParkingSlot();
        slot.setSlotStatus(ParkingSlot.SlotStatus.AVAILABLE);
        parkingSlotRepository.save(slot);

        carBookingDataRepository.save(booking);

        
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/occupancy/" + slot.getParking().getId(),
                    getOccupancyUpdate(slot.getParking().getId()));
        }

        bookingCancellationCounter.increment();
        log.info("Booking cancelled successfully: {}", id);
    }

    public BigDecimal calculateBookingCost(Long bookingId) {
        log.debug("Calculating cost for booking: {}", bookingId);

        CarBookingData booking = getBookingById(bookingId);
        long hours = ChronoUnit.HOURS.between(booking.getStartTime(), booking.getEndTime());
        if (hours == 0) hours = 1; 

        double hourlyRate = booking.getParkingSlot().getHourlyRate();
        double discount = 0.0;

        if (booking.getAppUser().getAppUserData() != null) {
            discount = booking.getAppUser().getAppUserData().getDiscountPercentage();
        }

        double total = hours * hourlyRate * (1 - discount / 100);

        log.debug("Booking cost calculated: {} for {} hours", total, hours);
        return BigDecimal.valueOf(total);
    }

    private OccupancyUpdate getOccupancyUpdate(Long parkingId) {
        List<ParkingSlot> slots = parkingSlotRepository.findByParkingId(parkingId);
        long available = slots.stream()
                .filter(s -> s.getSlotStatus() == ParkingSlot.SlotStatus.AVAILABLE)
                .count();
        return new OccupancyUpdate(parkingId, available, slots.size());
    }

    @Data
    @AllArgsConstructor
    public static class OccupancyUpdate {
        private Long parkingId;
        private long available;
        private long total;
    }
}
