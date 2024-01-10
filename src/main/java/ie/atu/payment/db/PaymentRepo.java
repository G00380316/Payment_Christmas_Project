package ie.atu.payment.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ie.atu.payment.model.Payment;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
    
    Optional<Payment> findByOrderID(long orderID);
    
}
