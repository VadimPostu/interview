package com.interview.resource;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interview.dto.PaymentCreationDto;
import com.interview.dto.PaymentDto;
import com.interview.dto.PaymentUpdateDto;
import com.interview.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RequestMapping("/api/payments")
@RestController
@Slf4j
public class PaymentResource {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentDto createPayment(@RequestBody @Valid PaymentCreationDto paymentCreationDto) {
        log.info("Creating payment with details: {}", paymentCreationDto);
        return paymentService.createPayment(paymentCreationDto);
    }

    @GetMapping
    public List<PaymentDto> getPayments() {
        log.info("Fetching payments");
        return paymentService.getPayments();
    }

    @PatchMapping(value = "/{paymentId}")
    public PaymentDto patchPayment(@PathVariable Long paymentId, @RequestBody @Valid PaymentUpdateDto paymentUpdateDto) {
        log.info("Update payment with Id: {} and details: {}", paymentId, paymentUpdateDto);
        return paymentService.updatePayment(paymentId, paymentUpdateDto);
    }

    @GetMapping(value = "/{paymentId}")
    public PaymentDto getPayment(@PathVariable Long paymentId) {
        log.info("Fetching payment with Id: {}", paymentId);
        return paymentService.getPayment(paymentId);
    }

    @DeleteMapping(value = "/{paymentId}")
    public void deletePayment(@PathVariable Long paymentId) {
        log.info("Removing payment with Id: {}", paymentId);
        paymentService.deletePayment(paymentId);
    }
}
