package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.PaymentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentDataRepository extends JpaRepository<PaymentData, Long> {
    Optional<PaymentData> findByBookingId(Long bookingId);
    Optional<PaymentData> findByTransactionId(String transactionId);
    List<PaymentData> findByUserId(Long userId);
}
