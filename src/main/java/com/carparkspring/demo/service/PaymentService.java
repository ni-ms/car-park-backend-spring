package com.carparkspring.demo.service;

import com.carparkspring.demo.model.PaymentData;
import com.carparkspring.demo.model.CarBookingData;
import com.carparkspring.demo.repository.PaymentDataRepository;
import com.carparkspring.demo.repository.CarBookingDataRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    private PaymentDataRepository paymentDataRepository;

    @Autowired
    private CarBookingDataRepository bookingRepository;

    private final Counter paymentCounter;
    private final Counter paymentFailureCounter;

    public PaymentService(MeterRegistry meterRegistry) {
        this.paymentCounter = Counter.builder("payments.completed")
                .description("Total number of completed payments")
                .register(meterRegistry);

        this.paymentFailureCounter = Counter.builder("payments.failed")
                .description("Total number of failed payments")
                .register(meterRegistry);
    }

    @Transactional
    public PaymentData processPayment(PaymentData payment) {
        log.info("Processing payment for booking: {}", payment.getBooking().getId());

        // Validate booking exists and is confirmed
        CarBookingData booking = bookingRepository.findById(payment.getBooking().getId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != CarBookingData.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Booking is not in confirmed status");
        }

        // Generate transaction ID
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setPaymentDateTime(LocalDateTime.now());
        payment.setStatus(PaymentData.PaymentStatus.PROCESSING);

        PaymentData savedPayment = paymentDataRepository.save(payment);

        try {
            // Simulate payment processing
            // In real scenario, integrate with payment gateway

            savedPayment.setStatus(PaymentData.PaymentStatus.COMPLETED);
            booking.setStatus(CarBookingData.BookingStatus.ACTIVE);
            bookingRepository.save(booking);

            paymentCounter.increment();
            log.info("Payment processed successfully: {}", savedPayment.getTransactionId());

        } catch (Exception e) {
            savedPayment.setStatus(PaymentData.PaymentStatus.FAILED);
            paymentFailureCounter.increment();
            log.error("Payment failed: {}", e.getMessage());
        }

        return paymentDataRepository.save(savedPayment);
    }

    @Cacheable(value = "payment", key = "#bookingId")
    public PaymentData getPaymentByBookingId(Long bookingId) {
        log.debug("Fetching payment for booking: {}", bookingId);
        return paymentDataRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking: " + bookingId));
    }

    public PaymentData getPaymentByTransactionId(String transactionId) {
        log.debug("Fetching payment by transaction id: {}", transactionId);
        return paymentDataRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found with transaction id: " + transactionId));
    }

    public List<PaymentData> getPaymentsByUserId(Long userId) {
        log.debug("Fetching payments for user: {}", userId);
        return paymentDataRepository.findByUserId(userId);
    }

    @Transactional
    public PaymentData refundPayment(String transactionId) {
        log.info("Processing refund for transaction: {}", transactionId);

        PaymentData payment = getPaymentByTransactionId(transactionId);

        if (payment.getStatus() != PaymentData.PaymentStatus.COMPLETED) {
            throw new RuntimeException("Only completed payments can be refunded");
        }

        payment.setStatus(PaymentData.PaymentStatus.REFUNDED);

        CarBookingData booking = payment.getBooking();
        booking.setStatus(CarBookingData.BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        log.info("Refund processed successfully for transaction: {}", transactionId);
        return paymentDataRepository.save(payment);
    }
}
