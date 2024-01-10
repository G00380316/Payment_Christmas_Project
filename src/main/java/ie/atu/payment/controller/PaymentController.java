package ie.atu.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ie.atu.payment.payload.PaymentRequest;
import ie.atu.payment.payload.PaymentResponse;
import ie.atu.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Log4j2
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("")
    public ResponseEntity<Long> takePayment(@RequestBody PaymentRequest paymentRequest) {
        log.info("Controller method Called to take Payment");
        log.info("Payment Request: " + paymentRequest.toString());

        if ( paymentRequest.getOrderID() != 0
                && paymentRequest.getAmount() >= 0) {
            try {
                long orderId = paymentService.proccessPayment(paymentRequest);
                log.info("Order processed Id: {}", orderId);
                return new ResponseEntity<>(orderId, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/order/{orderID}")
    public ResponseEntity<PaymentResponse> getOrderByID(@PathVariable("orderID") long orderID) {
        log.info("Controller method Called to get Order by ID");
        log.info("Order ID: " + orderID);

        PaymentResponse paymentResponse = paymentService.getPaymentDetailsByOrderID(orderID);
        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }
}
