package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.AppAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AppAdmin, Integer> {
    AppAdmin findByEmail(String username);
}
