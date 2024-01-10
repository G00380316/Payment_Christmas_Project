package ie.atu.payment.PaymentControllerTests;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ie.atu.payment.controller.PaymentController;
import ie.atu.payment.exception.PaymentServiceException;
import ie.atu.payment.payload.PaymentRequest;
import ie.atu.payment.payload.PaymentType;
import ie.atu.payment.service.PaymentService;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTestFails {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private PaymentService paymentService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void takePayment_shouldReturnBadRequest() throws Exception {
                PaymentRequest paymentRequest = new PaymentRequest(1L, "1234", PaymentType.PAYPAL, -100L);
                when(paymentService.proccessPayment(any(PaymentRequest.class))).thenReturn(1L);

                mockMvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                        .andExpect(status().isBadRequest());
        }

        @Test
        public void getPaymentByOrderID_shouldReturnisNotFound() throws Exception {
                long orderId = 1L;

                when(paymentService.getPaymentDetailsByOrderID(anyLong()))
                                .thenReturn(null)
                                .thenThrow(new PaymentServiceException("Error Code:", "PAYMENT_NOT_FOUND"));

                mockMvc.perform(get("/order/{orderId}", orderId))
                                .andExpect(status().isNotFound());
        }
}
