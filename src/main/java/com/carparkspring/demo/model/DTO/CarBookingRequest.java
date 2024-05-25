package com.carparkspring.demo.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarBookingRequest {
    private String carModel;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String lpnumber;
    private List<String> miscFacilities;
    private Long parkingId;
    private Integer userId;
}
