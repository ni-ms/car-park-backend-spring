import com.carparkspring.demo.model.AppUser;
import com.carparkspring.demo.model.CarBookingData;
import com.carparkspring.demo.model.PaymentData;
import com.carparkspring.demo.repository.PaymentDataRepository;
import com.carparkspring.demo.service.CarBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {
    private final PaymentDataRepository paymentRepository;
    private final CarBookingService bookingService;

    @Autowired
    public PaymentService(PaymentDataRepository paymentRepository,
                          CarBookingService bookingService) {
        this.paymentRepository = paymentRepository;
        this.bookingService = bookingService;
    }

    public PaymentData createPayment(AppUser user, CarBookingData booking,
                                     BigDecimal amount, String paymentMethod) {
        PaymentData payment = new PaymentData(user, booking, amount, paymentMethod);
        payment.setTransactionId(generateTransactionId());
        return paymentRepository.save(payment);
    }

    public PaymentData processPayment(Long paymentId) {
        PaymentData payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        // Add payment processing logic here
        payment.setStatus(PaymentData.PaymentStatus.COMPLETED);
        return paymentRepository.save(payment);
    }

    private String generateTransactionId() {
        // Implement transaction ID generation logic
        return "TXN" + System.currentTimeMillis();
    }
}
