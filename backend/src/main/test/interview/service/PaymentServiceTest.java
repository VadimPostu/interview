package interview.service;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.interview.dto.PaymentCreationDto;
import com.interview.dto.PaymentDto;
import com.interview.dto.PaymentUpdateDto;
import com.interview.entity.PaymentEntity;
import com.interview.exception.PaymentNotFoundException;
import com.interview.mapper.PaymentMapper;
import com.interview.repository.PaymentRepository;
import com.interview.service.PaymentService;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void testCreatePayment() {
        PaymentCreationDto paymentCreationDto = mock(PaymentCreationDto.class);
        PaymentEntity paymentEntity = mock(PaymentEntity.class);
        PaymentDto paymentDto = mock(PaymentDto.class);
        when(paymentMapper.mapDtoToEntity(paymentCreationDto)).thenReturn(paymentEntity);
        when(paymentMapper.mapEntityToDto(paymentEntity)).thenReturn(paymentDto);

        PaymentDto payment = paymentService.createPayment(paymentCreationDto);

        verify(paymentMapper).mapDtoToEntity(paymentCreationDto);
        verify(paymentRepository).saveAndFlush(paymentEntity);
        verify(paymentMapper).mapEntityToDto(paymentEntity);
        assertEquals(paymentDto, payment);
    }

    @Test
    void testUpdatePayment() {
        PaymentUpdateDto paymentUpdateDto = mock(PaymentUpdateDto.class);
        PaymentEntity paymentEntity = mock(PaymentEntity.class);
        PaymentDto paymentDto = mock(PaymentDto.class);
        when(paymentRepository.findById(1L)).thenReturn(of(paymentEntity));
        when(paymentMapper.mapEntityToDto(paymentEntity)).thenReturn(paymentDto);

        PaymentDto payment = paymentService.updatePayment(1L, paymentUpdateDto);

        verify(paymentRepository).findById(1L);
        verify(paymentMapper).updateEntityFromDto(paymentEntity, paymentUpdateDto);
        verify(paymentRepository).saveAndFlush(paymentEntity);
        verify(paymentMapper).mapEntityToDto(paymentEntity);
        assertEquals(paymentDto, payment);
    }

    @Test
    void testUpdatePayment_andPaymentNotFound() {
        PaymentUpdateDto paymentUpdateDto = mock(PaymentUpdateDto.class);
        when(paymentRepository.findById(1L)).thenReturn(empty());
        assertThrows(PaymentNotFoundException.class, () -> paymentService.updatePayment(1L, paymentUpdateDto));
    }

    @Test
    void testGetPayment() {
        PaymentEntity paymentEntity = mock(PaymentEntity.class);
        PaymentDto paymentDto = mock(PaymentDto.class);
        when(paymentRepository.findById(1L)).thenReturn(of(paymentEntity));
        when(paymentMapper.mapEntityToDto(paymentEntity)).thenReturn(paymentDto);

        PaymentDto payment = paymentService.getPayment(1L);

        verify(paymentRepository).findById(1L);
        verify(paymentMapper).mapEntityToDto(paymentEntity);
        assertEquals(paymentDto, payment);
    }

    @Test
    void testGetPayment_andPaymentNotFound() {
        when(paymentRepository.findById(1L)).thenReturn(empty());
        assertThrows(PaymentNotFoundException.class, () -> paymentService.getPayment(1L));
    }

    @Test
    void testGetPayments() {
        List<PaymentEntity> paymentEntityList = List.of(mock(PaymentEntity.class));
        List<PaymentDto> paymentDtoList = List.of(mock(PaymentDto.class));
        when(paymentRepository.findAll()).thenReturn(paymentEntityList);
        when(paymentMapper.mapEntityListToDtoList(paymentEntityList)).thenReturn(paymentDtoList);

        List<PaymentDto> payments = paymentService.getPayments();

        verify(paymentRepository).findAll();
        verify(paymentMapper).mapEntityListToDtoList(paymentEntityList);
        assertEquals(paymentDtoList, payments);
    }

    @Test
    void testDeletePayment() {
        paymentService.deletePayment(1L);
        verify(paymentRepository).deleteById(1L);
    }
}
