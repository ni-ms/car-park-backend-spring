package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmailId(String email);

    AppUser findByUsername(String username);

    List<AppUser> findByRole(String role);

    boolean existsByEmailId(String email);

    boolean existsByUsername(String username);
}
