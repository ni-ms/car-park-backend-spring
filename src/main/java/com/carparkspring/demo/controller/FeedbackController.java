package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.FeedbackData;
import com.carparkspring.demo.service.FeedBackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
@Tag(name = "Feedback", description = "Feedback management APIs")
public class FeedbackController {

    @Autowired
    private FeedBackService feedbackService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Create feedback", description = "Create feedback for a completed booking")
    public FeedbackData createFeedback(@RequestBody FeedbackData feedback) {
        return feedbackService.saveFeedback(feedback);
    }

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get feedback by booking", description = "Get feedback for a specific booking")
    public List<FeedbackData> getFeedbacksByBookingId(@PathVariable Long bookingId) {
        return feedbackService.getFeedbacksByBookingId(bookingId);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get feedback by user", description = "Get all feedback submitted by a user")
    public List<FeedbackData> getFeedbacksByUserId(@PathVariable Long userId) {
        return feedbackService.getFeedbacksByUserId(userId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get all feedback", description = "Get all feedback in the system")
    public List<FeedbackData> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("/parking/{parkingId}/rating")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get parking rating", description = "Get average rating for a parking facility")
    public Double getParkingAverageRating(@PathVariable Long parkingId) {
        return feedbackService.getAverageRatingForParking(parkingId);
    }
}
