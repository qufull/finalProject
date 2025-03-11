package com.example.demo;

import com.example.demo.dto.PaymentDto;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.enums.PaymentType;
import com.example.demo.exception.PaymentException;
import com.example.demo.exception.PaymentNotFoundException;
import com.example.demo.mapper.PaymentMapper;
import com.example.demo.model.Payment;
import com.example.demo.model.User;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentService paymentService;

    private User user;
    private Payment payment;
    private PaymentDto paymentDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        payment = new Payment();
        payment.setId(1L);
        payment.setAmount(100.0);
        payment.setPaymentType(PaymentType.REPLENISHMENT);
        payment.setStatus(PaymentStatus.APPROVED);

        paymentDto = new PaymentDto();
        paymentDto.setAmount(100.0);
        paymentDto.setPaymentType(PaymentType.REPLENISHMENT);
        paymentDto.setStatus(PaymentStatus.APPROVED);
    }

    @Test
    void testFindAllByUserId_Success() {
        when(paymentRepository.findByUserId(anyLong())).thenReturn(Optional.of(List.of(payment)));
        when(paymentMapper.toDtoList(anyList())).thenReturn(List.of(paymentDto));

        List<PaymentDto> result = paymentService.findAllByUserId(user);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100.0, result.get(0).getAmount());
        verify(paymentRepository, times(1)).findByUserId(anyLong());
    }

    @Test
    void testFindAllByUserId_NotFound() {
        when(paymentRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, () -> {
            paymentService.findAllByUserId(user);
        });

        assertEquals("Payments not found for user with id: 1", exception.getMessage());
        verify(paymentRepository, times(1)).findByUserId(anyLong());
    }

    @Test
    void testFindAllByUserId_Exception() {
        when(paymentRepository.findByUserId(anyLong())).thenThrow(new RuntimeException("Unexpected error"));

        PaymentException exception = assertThrows(PaymentException.class, () -> {
            paymentService.findAllByUserId(user);
        });

        assertEquals("Failed to fetch payments", exception.getMessage());
        verify(paymentRepository, times(1)).findByUserId(anyLong());
    }
}
