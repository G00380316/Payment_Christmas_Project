package ie.atu.payment.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PaymentServiceException extends RuntimeException{

    private String errorCode;

    public PaymentServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
