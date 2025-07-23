package com.interview.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class PaymentDto implements Serializable {

    private Long paymentId;

    private String customerId;

    private String currency;

    private BigDecimal amount;

    private String paymentMethodType;
}
