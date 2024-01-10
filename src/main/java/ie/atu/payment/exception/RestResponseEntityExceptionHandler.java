package ie.atu.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ie.atu.payment.payload.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(PaymentServiceException.class)
    public ResponseEntity<ErrorResponse> handleProductServiceException(PaymentServiceException exception) {
        new ErrorResponse();
        return new ResponseEntity<>(ErrorResponse.builder()
        .errorMessage(exception.getMessage())
        .errorCode(exception.getErrorCode())
                .build(), HttpStatus.NOT_FOUND);
    }
}
