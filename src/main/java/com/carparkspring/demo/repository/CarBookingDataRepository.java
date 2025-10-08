package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.CarBookingData;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarBookingDataRepository extends JpaRepository<CarBookingData, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT b FROM CarBookingData b WHERE b.id = :id")
    Optional<CarBookingData> findByIdWithLock(@Param("id") Long id);

    List<CarBookingData> findByAppUserId(Long userId);

    @Query("SELECT b FROM CarBookingData b WHERE b.parkingSlot.id = :slotId " +
            "AND b.status IN ('CONFIRMED', 'ACTIVE') " +
            "AND ((b.startTime <= :endTime AND b.endTime >= :startTime))")
    List<CarBookingData> findConflictingBookings(
            @Param("slotId") Long slotId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}