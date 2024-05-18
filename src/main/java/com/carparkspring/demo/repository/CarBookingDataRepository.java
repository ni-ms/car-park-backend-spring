package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.CarBookingData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarBookingDataRepository extends JpaRepository<CarBookingData, Long> {
}