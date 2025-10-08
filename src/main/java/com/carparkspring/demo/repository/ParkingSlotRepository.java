package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.ParkingSlot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    @Lock(LockModeType.OPTIMISTIC)
    Optional<ParkingSlot> findById(Long id);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT ps FROM ParkingSlot ps WHERE ps.id = :id")
    Optional<ParkingSlot> findByIdWithLock(Long id);

    List<ParkingSlot> findByParkingId(Long parkingId);

    @Query("SELECT ps FROM ParkingSlot ps WHERE ps.parking.id = :parkingId AND ps.slotStatus = :status")
    List<ParkingSlot> findAvailableSlotsByParkingIdAndStatus(Long parkingId, ParkingSlot.SlotStatus status);
}
