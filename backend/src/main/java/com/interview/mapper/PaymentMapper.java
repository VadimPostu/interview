package com.interview.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.interview.dto.PaymentCreationDto;
import com.interview.dto.PaymentDto;
import com.interview.dto.PaymentUpdateDto;
import com.interview.entity.PaymentEntity;

@Mapper
public interface PaymentMapper {

    @Mapping(source = "id", target = "paymentId")
    PaymentDto mapEntityToDto(PaymentEntity paymentEntity);

    PaymentEntity mapDtoToEntity(PaymentCreationDto paymentCreationDto);

    void updateEntityFromDto(@MappingTarget PaymentEntity entity, PaymentUpdateDto paymentUpdateDto);

    @Mapping(source = "id", target = "paymentId")
    List<PaymentDto> mapEntityListToDtoList(List<PaymentEntity> paymentEntityList);
}
