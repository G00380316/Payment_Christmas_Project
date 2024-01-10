package ie.atu.payment.PaymentServiceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ie.atu.payment.db.PaymentRepo;
import ie.atu.payment.model.Payment;
import ie.atu.payment.payload.PaymentRequest;
import ie.atu.payment.payload.PaymentResponse;
import ie.atu.payment.payload.PaymentType;
import ie.atu.payment.service.PaymentService;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTestSuccess {

    @Mock
    private PaymentRepo paymentRepo;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    public void testProcessPayment() {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderID(1L);
        paymentRequest.setAmount(100);
        paymentRequest.setReferenceNumber("REF123");
        paymentRequest.setPaymentType(PaymentType.CREDIT_CARD);

        Payment savedPayment = Payment.builder()
                .id(1L)
                .paymentDate(Instant.now())
                .paymentType(paymentRequest.getPaymentType().name())
                .paymentStatus("SUCCESS")
                .orderID(paymentRequest.getOrderID())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount())
                .build();

        when(paymentRepo.save(any(Payment.class))).thenReturn(savedPayment);

        // Act
        long paymentId = paymentService.proccessPayment(paymentRequest);

        // Assert
        assertEquals(savedPayment.getId(), paymentId);
    }

    @Test
    public void testGetPaymentDetailsByOrderID() {
        // Arrange
        long orderID = 1L;
        Payment mockPayment = Payment.builder()
                .id(1L)
                .paymentDate(Instant.now())
                .paymentType("CREDIT_CARD")
                .paymentStatus("SUCCESS")
                .orderID(orderID)
                .referenceNumber("REF123")
                .amount(100)
                .build();

        when(paymentRepo.findByOrderID(orderID)).thenReturn(Optional.of(mockPayment));

        // Act
        PaymentResponse paymentResponse = paymentService.getPaymentDetailsByOrderID(orderID);

        // Assert
        assertEquals(mockPayment.getId(), paymentResponse.getPaymentID());
        assertEquals(PaymentType.CREDIT_CARD, paymentResponse.getPaymentType());
        assertEquals(mockPayment.getPaymentDate(), paymentResponse.getPaymentDate());
        assertEquals(mockPayment.getOrderID(), paymentResponse.getOrderID());
        assertEquals(mockPayment.getPaymentStatus(), paymentResponse.getStatus());
        assertEquals(mockPayment.getAmount(), paymentResponse.getAmount());
    }
}
