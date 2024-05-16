package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Integer> {
    AppUser findByEmail(String username);
}
