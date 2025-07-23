package interview.mapper;

import static interview.TestUtils.buildPaymentCreationDto;
import static interview.TestUtils.buildPaymentEntity;
import static interview.TestUtils.buildPaymentUpdateDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.interview.dto.PaymentCreationDto;
import com.interview.dto.PaymentDto;
import com.interview.dto.PaymentUpdateDto;
import com.interview.entity.PaymentEntity;
import com.interview.mapper.PaymentMapper;

public class PaymentMapperTest {

    private PaymentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PaymentMapper.class);
    }

    @Test
    void testMapEntityToDto() {
        PaymentEntity entity = buildPaymentEntity();

        PaymentDto dto = mapper.mapEntityToDto(entity);

        assertPaymentDtoMatchesEntity(entity, dto);
    }

    @Test
    void testMapDtoToEntity() {
        PaymentCreationDto creationDto = buildPaymentCreationDto();

        PaymentEntity entity = mapper.mapDtoToEntity(creationDto);

        assertNull(entity.getId());
        assertEquals(creationDto.getCustomerId(), entity.getCustomerId());
        assertEquals(creationDto.getAmount(), entity.getAmount());
        assertEquals(creationDto.getCurrency(), entity.getCurrency());
        assertEquals(creationDto.getPaymentMethodType(), entity.getPaymentMethodType());
    }

    @Test
    void testUpdateEntityFromDto() {
        PaymentEntity entity = buildPaymentEntity(1L, "customerId", BigDecimal.ONE, "EUR", "APPLE_PAY");
        PaymentUpdateDto updateDto = buildPaymentUpdateDto();

        mapper.updateEntityFromDto(entity, updateDto);

        assertEquals(new BigDecimal("15.34"), entity.getAmount());
        assertEquals("USD", entity.getCurrency());
        assertEquals("CARD", entity.getPaymentMethodType());
        assertEquals("customerId", entity.getCustomerId());
    }

    @Test
    void testMapEntityListToDtoList() {
        PaymentEntity entity1 = buildPaymentEntity(1L, "customerId1", new BigDecimal("12.34"), "USD", "APPLE_PAY");
        PaymentEntity entity2 = buildPaymentEntity(2L, "customerId2", new BigDecimal("20.00"), "EUR", "CARD");

        List<PaymentDto> dtoList = mapper.mapEntityListToDtoList(Arrays.asList(entity1, entity2));

        assertEquals(2, dtoList.size());
        assertPaymentDtoMatchesEntity(entity1, dtoList.get(0));
        assertPaymentDtoMatchesEntity(entity2, dtoList.get(1));
    }

    private static void assertPaymentDtoMatchesEntity(PaymentEntity entity, PaymentDto dto) {
        assertEquals(entity.getId(), dto.getPaymentId());
        assertEquals(entity.getCustomerId(), dto.getCustomerId());
        assertEquals(entity.getAmount(), dto.getAmount());
        assertEquals(entity.getCurrency(), dto.getCurrency());
        assertEquals(entity.getPaymentMethodType(), dto.getPaymentMethodType());
    }
}
