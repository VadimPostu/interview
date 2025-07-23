package com.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interview.entity.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

}
