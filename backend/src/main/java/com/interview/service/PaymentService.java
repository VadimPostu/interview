package com.interview.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.interview.dto.PaymentCreationDto;
import com.interview.dto.PaymentDto;
import com.interview.dto.PaymentUpdateDto;
import com.interview.entity.PaymentEntity;
import com.interview.exception.PaymentNotFoundException;
import com.interview.mapper.PaymentMapper;
import com.interview.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    public PaymentDto createPayment(PaymentCreationDto paymentCreationDto) {
        PaymentEntity paymentEntity = paymentMapper.mapDtoToEntity(paymentCreationDto);
        paymentRepository.saveAndFlush(paymentEntity);

        return paymentMapper.mapEntityToDto(paymentEntity);
    }

    public PaymentDto updatePayment(Long paymentId, PaymentUpdateDto paymentUpdateDto) {
        PaymentEntity paymentEntity = paymentRepository.findById(paymentId)
            .orElseThrow(PaymentNotFoundException::new);
        paymentMapper.updateEntityFromDto(paymentEntity, paymentUpdateDto);
        paymentRepository.saveAndFlush(paymentEntity);

        return paymentMapper.mapEntityToDto(paymentEntity);
    }

    public PaymentDto getPayment(Long paymentId) {
        PaymentEntity paymentEntity = paymentRepository.findById(paymentId)
            .orElseThrow(PaymentNotFoundException::new);

        return paymentMapper.mapEntityToDto(paymentEntity);
    }

    public List<PaymentDto> getPayments() {
        List<PaymentEntity> paymentEntityList = paymentRepository.findAll();

        return paymentMapper.mapEntityListToDtoList(paymentEntityList);
    }

    public void deletePayment(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }
}
