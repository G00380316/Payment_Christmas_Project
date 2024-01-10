package ie.atu.payment.PaymentRepoTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ie.atu.payment.db.PaymentRepo;
import ie.atu.payment.model.Payment;
import ie.atu.payment.exception.PaymentServiceException;

@SpringBootTest
public class PaymentRepoTest {

    @Autowired
    private PaymentRepo paymentRepo;

    @Test
    public void shouldSaveAndRetrieveProduct() {

        Instant timeTest = Instant.now();

        // Given
        Payment payment = new Payment(1L,2L,"PAYPAL","1234",timeTest.truncatedTo(ChronoUnit.SECONDS),"SUCCESS",100);

        // When
        Payment savedPayment = paymentRepo.save(payment);

        // Then
        Optional<Payment> retrievedPayment = paymentRepo.findById(savedPayment.getId());
        assertThat(retrievedPayment).isPresent();
        assertThat(retrievedPayment.get().getId()).isEqualTo(1L);
        assertThat(retrievedPayment.get().getOrderID()).isEqualTo(2L);
        assertThat(retrievedPayment.get().getPaymentType()).isEqualTo("PAYPAL");
        assertThat(retrievedPayment.get().getReferenceNumber()).isEqualTo("1234");
        assertThat(retrievedPayment.get().getPaymentDate()).isEqualTo(payment.getPaymentDate().truncatedTo(ChronoUnit.SECONDS));
        assertThat(retrievedPayment.get().getPaymentStatus()).isEqualTo("SUCCESS");
        assertThat(retrievedPayment.get().getAmount()).isEqualTo(100);
    }

    @Test
    public void shouldThrowExceptionWhenProductIdNotFound() {
        // Given
        long nonExistentProductId = 999L;

        // When/Then
        assertThrows(PaymentServiceException.class, () -> paymentRepo.findById(nonExistentProductId)
                .orElseThrow(() -> new PaymentServiceException("Payment not found", "PAYMENT_NOT_FOUND")));
    }
}
