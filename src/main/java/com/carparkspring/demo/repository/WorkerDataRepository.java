package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.WorkerData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkerDataRepository extends JpaRepository<WorkerData, Long> {

    List<WorkerData> findByParkingId(Long parkingId);

    List<WorkerData> findByOnDutyTrue();
}
