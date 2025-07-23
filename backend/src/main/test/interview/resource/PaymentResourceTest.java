package interview.resource;

import static interview.TestUtils.buildPaymentCreationDto;
import static interview.TestUtils.buildPaymentDto;
import static interview.TestUtils.buildPaymentUpdateDto;
import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.dto.PaymentCreationDto;
import com.interview.dto.PaymentDto;
import com.interview.dto.PaymentUpdateDto;
import com.interview.resource.PaymentResource;
import com.interview.service.PaymentService;

@ExtendWith(MockitoExtension.class)
public class PaymentResourceTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentResource paymentResource;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentResource).build();
    }

    @Test
    void testCreatePayment() throws Exception {
        PaymentCreationDto paymentCreationDto = buildPaymentCreationDto();
        PaymentDto paymentDto = buildPaymentDto();
        when(paymentService.createPayment(any(PaymentCreationDto.class))).thenReturn(paymentDto);

        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentCreationDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.paymentId").value(1L))
            .andExpect(jsonPath("$.customerId").value("customerId"))
            .andExpect(jsonPath("$.amount").value("12.34"))
            .andExpect(jsonPath("$.currency").value("USD"))
            .andExpect(jsonPath("$.paymentMethodType").value("CARD"));
    }

    @Test
    void testGetPayments() throws Exception {
        PaymentDto paymentDto1 = buildPaymentDto(1L, "customerId1", new BigDecimal("12.34"), "USD", "CARD");
        PaymentDto paymentDto2 = buildPaymentDto(2L, "customerId2", BigDecimal.ONE, "EUR", "APPLE_PAY");

        when(paymentService.getPayments()).thenReturn(asList(paymentDto1, paymentDto2));

        mockMvc.perform(get("/api/payments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].paymentId").value(1L))
            .andExpect(jsonPath("$[0].customerId").value("customerId1"))
            .andExpect(jsonPath("$[0].amount").value("12.34"))
            .andExpect(jsonPath("$[0].currency").value("USD"))
            .andExpect(jsonPath("$[0].paymentMethodType").value("CARD"))
            .andExpect(jsonPath("$[1].paymentId").value(2L))
            .andExpect(jsonPath("$[1].customerId").value("customerId2"))
            .andExpect(jsonPath("$[1].amount").value("1"))
            .andExpect(jsonPath("$[1].currency").value("EUR"))
            .andExpect(jsonPath("$[1].paymentMethodType").value("APPLE_PAY"));
    }

    @Test
    void testGetPayment() throws Exception {
        PaymentDto paymentDto = buildPaymentDto();

        when(paymentService.getPayment(eq(1L))).thenReturn(paymentDto);

        mockMvc.perform(get("/api/payments/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.paymentId").value("1"))
            .andExpect(jsonPath("$.customerId").value("customerId"))
            .andExpect(jsonPath("$.amount").value("12.34"))
            .andExpect(jsonPath("$.currency").value("USD"))
            .andExpect(jsonPath("$.paymentMethodType").value("CARD"));
    }

    @Test
    void testPatchPayment() throws Exception {
        PaymentUpdateDto updateDto = buildPaymentUpdateDto();
        PaymentDto paymentDto = buildPaymentDto();

        when(paymentService.updatePayment(eq(1L), any(PaymentUpdateDto.class))).thenReturn(paymentDto);

        mockMvc.perform(patch("/api/payments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.paymentId").value("1"))
            .andExpect(jsonPath("$.customerId").value("customerId"))
            .andExpect(jsonPath("$.amount").value("12.34"))
            .andExpect(jsonPath("$.currency").value("USD"))
            .andExpect(jsonPath("$.paymentMethodType").value("CARD"));
    }

    @Test
    void testDeletePayment() throws Exception {
        doNothing().when(paymentService).deletePayment(eq(1L));

        mockMvc.perform(delete("/api/payments/1"))
            .andExpect(status().isOk());
    }
}
