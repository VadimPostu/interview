package interview.integration;

import static interview.TestUtils.buildPaymentCreationDto;
import static interview.TestUtils.buildPaymentUpdateDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.Application;
import com.interview.dto.PaymentCreationDto;
import com.interview.dto.PaymentDto;
import com.interview.dto.PaymentUpdateDto;
import com.interview.entity.PaymentEntity;
import com.interview.repository.PaymentRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@AutoConfigureMockMvc
public class PaymentResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateAndGetPayment() throws Exception {
        // Prepare creation request
        PaymentCreationDto paymentCreationDto = buildPaymentCreationDto();

        // Execute request and assert response
        String response = mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentCreationDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.paymentId").exists())
            .andExpect(jsonPath("$.customerId").value("customerId"))
            .andExpect(jsonPath("$.amount").value("12.34"))
            .andExpect(jsonPath("$.currency").value("USD"))
            .andExpect(jsonPath("$.paymentMethodType").value("CARD"))
            .andReturn().getResponse().getContentAsString();
        PaymentDto paymentDto = objectMapper.readValue(response, PaymentDto.class);

        // Assert stored payment entity
        PaymentEntity paymentEntity = paymentRepository.findById(paymentDto.getPaymentId()).orElse(null);
        assertNotNull(paymentEntity);
        assertEquals("customerId", paymentEntity.getCustomerId());
        assertEquals(new BigDecimal("12.34"), paymentEntity.getAmount());
        assertEquals("USD", paymentEntity.getCurrency());
        assertEquals("CARD", paymentEntity.getPaymentMethodType());

        // Get the payment by id and assert entity
        mockMvc.perform(get("/api/payments/" + paymentDto.getPaymentId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.paymentId").value(paymentDto.getPaymentId()))
            .andExpect(jsonPath("$.customerId").value("customerId"))
            .andExpect(jsonPath("$.amount").value("12.34"))
            .andExpect(jsonPath("$.currency").value("USD"))
            .andExpect(jsonPath("$.paymentMethodType").value("CARD"));
    }

    @Test
    void testUpdatePayment() throws Exception {
        // Prepare creation request
        PaymentCreationDto paymentCreationDto = buildPaymentCreationDto();

        // Execute request and assert response
        String response = mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentCreationDto)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        PaymentDto paymentDto = objectMapper.readValue(response, PaymentDto.class);

        // Prepare update request
        PaymentUpdateDto paymentUpdateDto = buildPaymentUpdateDto();

        // Execute update request and assert response
        mockMvc.perform(patch("/api/payments/" + paymentDto.getPaymentId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentUpdateDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.paymentId").value(paymentDto.getPaymentId()))
            .andExpect(jsonPath("$.customerId").value("customerId"))
            .andExpect(jsonPath("$.amount").value("15.34"))
            .andExpect(jsonPath("$.currency").value("USD"))
            .andExpect(jsonPath("$.paymentMethodType").value("CARD"));

        // Get the payment by id and assert entity
        PaymentEntity entity = paymentRepository.findById(paymentDto.getPaymentId()).orElse(null);
        assertNotNull(entity);
        assertEquals("customerId", entity.getCustomerId());
        assertEquals(new BigDecimal("15.34"), entity.getAmount());
        assertEquals("USD", entity.getCurrency());
        assertEquals("CARD", entity.getPaymentMethodType());
    }

    @Test
    void testGetPayments() throws Exception {
        // Prepare creation requests
        PaymentCreationDto creationDto1 = buildPaymentCreationDto("customerId1", new BigDecimal("12.34"), "USD", "CARD");
        PaymentCreationDto creationDto2 = buildPaymentCreationDto("customerId2", new BigDecimal("13.34"), "EUR", "APPLE_PAY");

        // Execute creation requests and assert responses
        String contentAsString1 = mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creationDto1)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        PaymentDto paymentDto1 = objectMapper.readValue(contentAsString1, PaymentDto.class);

        String contentAsString2 = mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creationDto2)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        PaymentDto paymentDto2 = objectMapper.readValue(contentAsString2, PaymentDto.class);

        // Get all payments and assert response
        mockMvc.perform(get("/api/payments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].paymentId").value(paymentDto1.getPaymentId()))
            .andExpect(jsonPath("$[0].customerId").value("customerId1"))
            .andExpect(jsonPath("$[0].amount").value("12.34"))
            .andExpect(jsonPath("$[0].currency").value("USD"))
            .andExpect(jsonPath("$[0].paymentMethodType").value("CARD"))
            .andExpect(jsonPath("$[1].paymentId").value(paymentDto2.getPaymentId()))
            .andExpect(jsonPath("$[1].customerId").value("customerId2"))
            .andExpect(jsonPath("$[1].amount").value("13.34"))
            .andExpect(jsonPath("$[1].currency").value("EUR"))
            .andExpect(jsonPath("$[1].paymentMethodType").value("APPLE_PAY"));

        // Get the payments by id and assert entities
        PaymentEntity entity1 = paymentRepository.findById(paymentDto1.getPaymentId()).orElse(null);
        PaymentEntity entity2 = paymentRepository.findById(paymentDto2.getPaymentId()).orElse(null);
        assertNotNull(entity1);
        assertNotNull(entity2);
        assertEquals("customerId1", entity1.getCustomerId());
        assertEquals(new BigDecimal("12.34"), entity1.getAmount());
        assertEquals("USD", entity1.getCurrency());
        assertEquals("CARD", entity1.getPaymentMethodType());
        assertEquals("customerId2", entity2.getCustomerId());
        assertEquals(new BigDecimal("13.34"), entity2.getAmount());
        assertEquals("EUR", entity2.getCurrency());
        assertEquals("APPLE_PAY", entity2.getPaymentMethodType());
    }

    @Test
    void testDeletePayment() throws Exception {
        // Prepare creation request
        PaymentCreationDto paymentCreationDto = buildPaymentCreationDto();

        // Execute request and assert response
        String response = mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentCreationDto)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        PaymentDto paymentDto = objectMapper.readValue(response, PaymentDto.class);

        // Perform delete request
        mockMvc.perform(delete("/api/payments/" + paymentDto.getPaymentId()))
            .andExpect(status().isOk());

        // Verify that the database entity was correctly deleted
        assertFalse(paymentRepository.findById(paymentDto.getPaymentId()).isPresent());

        // Verify that retrieving the deleted payment returns 404
        mockMvc.perform(get("/api/payments/" + paymentDto.getPaymentId()))
            .andExpect(status().is(404))
            .andExpect(jsonPath("$.errorCode").value("1000"))
            .andExpect(jsonPath("$.errorMessage").value("Payment not found"));
    }

    @Test
    void testTypeMismatch() throws Exception {
        // Execute type mismatch request and assert response
        mockMvc.perform(get("/api/payments/14124asfadga"))
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.errorCode").value("1001"))
            .andExpect(jsonPath("$.errorMessage").value("Type mismatch"));
    }

    @Test
    void testInvalidRequest() throws Exception {
        // Prepare invalid creation request
        String invalidCreationPayload = """
            {
                "customerId": "",
                "amount": 13.2,
                "currency": "EUR",
                "paymentMethodType": "ApplePay"
            }""";

        // Execute request and assert response
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidCreationPayload))
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.errorCode").value("1002"))
            .andExpect(jsonPath("$.errorMessage").value("Invalid request. Customer id cannot be blank!"));
    }

    @Test
    void testUnhandledException() throws Exception {
        // Prepare unhandled exception creation request
        String invalidCreationPayload = """
            {
                "customerId": "",
                "amount": unhandled,
                "currency": "EUR",
                "paymentMethodType": "ApplePay"
            }""";

        // Execute request and assert response
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidCreationPayload))
            .andExpect(status().is(500))
            .andExpect(jsonPath("$.errorCode").value("9000"))
            .andExpect(jsonPath("$.errorMessage").value("Internal server error"));
    }
}
