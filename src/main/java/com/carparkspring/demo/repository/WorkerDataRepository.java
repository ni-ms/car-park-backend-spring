package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.WorkerData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkerDataRepository extends JpaRepository<WorkerData, Long> {
    Optional<WorkerData> findByName(String name);
}
