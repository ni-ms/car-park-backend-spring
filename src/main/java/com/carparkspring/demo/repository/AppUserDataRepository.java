package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.AppUserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserDataRepository extends JpaRepository<AppUserData, Long> {
    AppUserData findByAppUserId(Long appUserId);
}
