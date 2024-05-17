package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.AppAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<AppAdmin, Integer> {
    Optional<AppAdmin> findByEmailId(String email);
}
