package com.carparkspring.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "bookingId", referencedColumnName = "id")
    private CarBookingData booking;

    private int rating;
    private String comments;
    private LocalDateTime feedbackTime;
}