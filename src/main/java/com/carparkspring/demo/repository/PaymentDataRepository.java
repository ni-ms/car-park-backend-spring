package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.AppUser;
import com.carparkspring.demo.model.CarBookingData;
import com.carparkspring.demo.model.PaymentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentDataRepository extends JpaRepository<PaymentData, Long> {
    List<PaymentData> findByUser(AppUser user);

    Optional<PaymentData> findByBooking(CarBookingData booking);

    List<PaymentData> findByStatus(PaymentData.PaymentStatus status);

    Optional<PaymentData> findByTransactionId(String transactionId);
}
