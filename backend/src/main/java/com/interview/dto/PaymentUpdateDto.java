package com.interview.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import lombok.Data;

@Data
public class PaymentUpdateDto implements Serializable {

    @Positive(message = "Amount must be a positive number!")
    private BigDecimal amount;

    @NotBlank(message = "Currency cannot be blank!")
    private String currency;

    @NotBlank(message = "Payment method type cannot be blank!")
    private String paymentMethodType;
}
