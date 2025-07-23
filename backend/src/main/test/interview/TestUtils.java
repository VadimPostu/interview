package interview;

import java.math.BigDecimal;

import com.interview.dto.PaymentCreationDto;
import com.interview.dto.PaymentDto;
import com.interview.dto.PaymentUpdateDto;
import com.interview.entity.PaymentEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {

    public static PaymentCreationDto buildPaymentCreationDto() {
        return buildPaymentCreationDto("customerId", new BigDecimal("12.34"), "USD", "CARD");
    }

    public static PaymentCreationDto buildPaymentCreationDto(String customerId, BigDecimal amount, String currency, String paymentMethodType) {
        PaymentCreationDto paymentCreationDto = new PaymentCreationDto();
        paymentCreationDto.setCustomerId(customerId);
        paymentCreationDto.setAmount(amount);
        paymentCreationDto.setCurrency(currency);
        paymentCreationDto.setPaymentMethodType(paymentMethodType);

        return paymentCreationDto;
    }

    public static PaymentUpdateDto buildPaymentUpdateDto() {
        PaymentUpdateDto updateDto = new PaymentUpdateDto();
        updateDto.setAmount(new BigDecimal("15.34"));
        updateDto.setCurrency("USD");
        updateDto.setPaymentMethodType("CARD");

        return updateDto;
    }

    public static PaymentDto buildPaymentDto() {
        return buildPaymentDto(1L, "customerId", new BigDecimal("12.34"), "USD", "CARD");
    }

    public static PaymentDto buildPaymentDto(Long paymentId, String customerId, BigDecimal amount, String currency, String paymentMethodType) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentId(paymentId);
        paymentDto.setCustomerId(customerId);
        paymentDto.setAmount(amount);
        paymentDto.setCurrency(currency);
        paymentDto.setPaymentMethodType(paymentMethodType);

        return paymentDto;
    }

    public static PaymentEntity buildPaymentEntity() {
        return buildPaymentEntity(1L, "customerId", new BigDecimal("12.34"), "USD", "CARD");
    }

    public static PaymentEntity buildPaymentEntity(Long id, String customerId, BigDecimal amount, String currency, String paymentMethodType) {
        PaymentEntity entity = new PaymentEntity();
        entity.setId(id);
        entity.setCustomerId(customerId);
        entity.setAmount(amount);
        entity.setCurrency(currency);
        entity.setPaymentMethodType(paymentMethodType);

        return entity;
    }
}
