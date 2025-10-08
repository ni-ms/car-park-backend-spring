package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.AppAdmin;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface AdminRepository extends JpaRepository<AppAdmin, Integer> {
    Optional<AppAdmin> findByUsername(String username);

    Optional<AppAdmin> findByEmailId(String emailId);
    // Add this method
    boolean existsByUsername(String username);
}
