package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.FeedbackData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackDataRepository extends JpaRepository<FeedbackData, Long> {
    List<FeedbackData> findByBooking_ParkingSlot_Parking_Id(Long parkingId);

    List<FeedbackData> findByUserFeedbackId(Long userId);

    List<FeedbackData> findByBookingFeedbackId(Long bookingId);
}