package ie.atu.payment.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import ie.atu.payment.db.PaymentRepo;
import ie.atu.payment.exception.PaymentServiceException;
import ie.atu.payment.model.Payment;
import ie.atu.payment.payload.PaymentRequest;
import ie.atu.payment.payload.PaymentResponse;
import ie.atu.payment.payload.PaymentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentRepo paymentRepo;

    public long proccessPayment(PaymentRequest paymentRequest) {

        log.info("Procces Payment method has been called");
        log.info("Recording Payment Details: {}", paymentRequest);

        Payment payment = Payment.builder().paymentDate(Instant.now())
                .paymentType(paymentRequest.getPaymentType().name())
                .paymentStatus("SUCCESS")
                .orderID(paymentRequest.getOrderID())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount())
                .build();

        payment = paymentRepo.save(payment);

        log.info("Transaction has been Completed with Id: {}", payment.getId());

        return payment.getId();
    }
    
    public PaymentResponse getPaymentDetailsByOrderID(long orderID) {
        
        log.info("Reteiving Payment Details by Order ID method has been called");
        log.info("Getting Payment Details for the Order ID: {}", orderID);

        Payment payment = paymentRepo.findByOrderID(orderID)
                .orElseThrow(() -> new PaymentServiceException("Transcation with given id not found",
                        "TRANSACTION_NOT_FOUND"));
        
        PaymentResponse paymentResponse = PaymentResponse.builder().paymentID(payment.getId())
        .paymentType(PaymentType.valueOf(payment.getPaymentType()))
        .paymentDate(payment.getPaymentDate()).orderID(payment.getOrderID()).status(payment.getPaymentStatus())
                .amount(payment.getAmount()).build();

        log.info("Payment Response: {}", paymentResponse.toString());

        return paymentResponse;
    }
}
