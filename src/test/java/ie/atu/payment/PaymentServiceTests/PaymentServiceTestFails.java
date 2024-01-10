package ie.atu.payment.PaymentServiceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ie.atu.payment.db.PaymentRepo;
import ie.atu.payment.exception.PaymentServiceException;
import ie.atu.payment.model.Payment;
import ie.atu.payment.payload.PaymentRequest;
import ie.atu.payment.payload.PaymentType;
import ie.atu.payment.service.PaymentService;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTestFails {

    @Mock
    private PaymentRepo paymentRepo;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    public void testFailedProcessPayment() {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderID(1L);
        paymentRequest.setAmount(100);
        paymentRequest.setReferenceNumber("REF123");
        paymentRequest.setPaymentType(PaymentType.CREDIT_CARD);

        when(paymentRepo.save(any(Payment.class))).thenThrow(new RuntimeException("Simulating save failure"));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            paymentService.proccessPayment(paymentRequest);
        });
    }

    @Test
    public void testGetPaymentDetailsByOrderIDNotFound() {
        // Arrange
        long orderID = 1L;

        when(paymentRepo.findByOrderID(orderID)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(PaymentServiceException.class, () -> {
            paymentService.getPaymentDetailsByOrderID(orderID);
        });
    }
}
