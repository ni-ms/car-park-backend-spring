package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {
}