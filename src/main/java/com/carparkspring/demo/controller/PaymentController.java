package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.PaymentData;
import com.carparkspring.demo.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@Tag(name = "Payment", description = "Payment management APIs")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Process payment", description = "Process payment for a booking")
    public PaymentData processPayment(@RequestBody PaymentData payment) {
        return paymentService.processPayment(payment);
    }

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get payment by booking", description = "Get payment details for a specific booking")
    public PaymentData getPaymentByBookingId(@PathVariable Long bookingId) {
        return paymentService.getPaymentByBookingId(bookingId);
    }

    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get payment by transaction", description = "Get payment details by transaction ID")
    public PaymentData getPaymentByTransactionId(@PathVariable String transactionId) {
        return paymentService.getPaymentByTransactionId(transactionId);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get payments by user", description = "Get all payments for a specific user")
    public List<PaymentData> getPaymentsByUserId(@PathVariable Long userId) {
        return paymentService.getPaymentsByUserId(userId);
    }

    @PostMapping("/refund/{transactionId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Process refund", description = "Process refund for a payment")
    public PaymentData refundPayment(@PathVariable String transactionId) {
        return paymentService.refundPayment(transactionId);
    }
}
