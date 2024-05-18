package com.carparkspring.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carparkspring.demo.model.AppUserData;

public interface AppUserDataRepository extends JpaRepository<AppUserData, Long> {
}
