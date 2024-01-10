package ie.atu.payment.PaymentControllerTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import ie.atu.payment.controller.PaymentController;
import ie.atu.payment.payload.PaymentRequest;
import ie.atu.payment.payload.PaymentResponse;
import ie.atu.payment.payload.PaymentType;
import ie.atu.payment.service.PaymentService;


@WebMvcTest(PaymentController.class)
public class PaymentControllerTestSuccess {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void takePayment_shouldReturnCreated() throws Exception {
        PaymentRequest paymentRequest = new PaymentRequest(1L, "1234", PaymentType.PAYPAL, 100L);
        when(paymentService.proccessPayment(any(PaymentRequest.class))).thenReturn(1L);

        mockMvc.perform(post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isCreated());

        verify(paymentService, times(1)).proccessPayment(any(PaymentRequest.class));
    }

    @Test
    public void getPaymentByOrderID_shouldReturnOk() throws Exception {
        long orderId = 1L;
        PaymentResponse paymentResponse = new PaymentResponse(1L,"SUCCESS",PaymentType.PAYPAL, 100, Instant.now(), 2L);
        when(paymentService.getPaymentDetailsByOrderID(anyLong())).thenReturn(paymentResponse);

        MvcResult result = mockMvc.perform(get("/payment/order/{orderID}", orderId))
            .andExpect(status().isOk())
            .andReturn();

        verify(paymentService, times(1)).getPaymentDetailsByOrderID(orderId);

            String content = result.getResponse().getContentAsString();
            PaymentResponse actualResponse = objectMapper.readValue(content, PaymentResponse.class);

            assertEquals(paymentResponse, actualResponse);
    }
}
