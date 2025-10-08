package com.carparkspring.demo.service;

import com.carparkspring.demo.model.FeedbackData;
import com.carparkspring.demo.model.CarBookingData;
import com.carparkspring.demo.repository.FeedbackDataRepository;
import com.carparkspring.demo.repository.CarBookingDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class FeedBackService {

    @Autowired
    private FeedbackDataRepository feedbackRepository;

    @Autowired
    private CarBookingDataRepository bookingRepository;

    @Transactional
    public FeedbackData saveFeedback(FeedbackData feedback) {
        log.info("Saving feedback for booking: {}", feedback.getBooking().getId());


        if (feedback.getRating() < 1 || feedback.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }


        CarBookingData booking = bookingRepository.findById(feedback.getBooking().getId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != CarBookingData.BookingStatus.COMPLETED) {
            throw new RuntimeException("Can only provide feedback for completed bookings");
        }

        feedback.setFeedbackTime(LocalDateTime.now());

        FeedbackData saved = feedbackRepository.save(feedback);
        log.info("Feedback saved successfully with id: {}", saved.getFeedbackId());

        return saved;
    }

    @Cacheable(value = "feedbacks", key = "#bookingId")
    public List<FeedbackData> getFeedbacksByBookingId(Long bookingId) {
        log.debug("Fetching feedbacks for booking: {}", bookingId);
        return feedbackRepository.findByBooking_Id(bookingId);
    }

    public List<FeedbackData> getFeedbacksByUserId(Long userId) {
        log.debug("Fetching feedbacks for user: {}", userId);
        return feedbackRepository.findByUser_Id(userId);
    }

    public List<FeedbackData> getAllFeedbacks() {
        log.debug("Fetching all feedbacks");
        return feedbackRepository.findAll();
    }

    public Double getAverageRatingForParking(Long parkingId) {
        log.debug("Calculating average rating for parking: {}", parkingId);

        List<FeedbackData> feedbacks = feedbackRepository.findByBooking_ParkingSlot_Parking_Id(parkingId);

        if (feedbacks.isEmpty()) {
            return 0.0;
        }

        double average = feedbacks.stream()
                .mapToInt(FeedbackData::getRating)
                .average()
                .orElse(0.0);

        log.debug("Average rating for parking {}: {}", parkingId, average);
        return average;
    }
}
