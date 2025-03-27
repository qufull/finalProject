package com.example.demo;

import com.example.demo.dto.DepositDto;
import com.example.demo.exception.CredentialNotFoundException;
import com.example.demo.mapper.PaymentMapper;
import com.example.demo.model.Currency;
import com.example.demo.model.Payment;
import com.example.demo.model.User;
import com.example.demo.repository.CurrencyRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private BalanceService balanceService;

    private User user;
    private DepositDto depositDto;
    private Currency currency;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setBalance(100.0);

        depositDto = new DepositDto();
        depositDto.setAmount(50.0);
        depositDto.setCurrencyCode("USD");

        currency = new Currency();
        currency.setCurrencyCode("USD");
        currency.setExchangeRate(1.0);
    }

    @Test
    void testDeposit_Success() {
        when(currencyRepository.findByCurrencyCode("USD")).thenReturn(Optional.of(currency));
        when(userRepository.findUserWithCredentialsById(any(Long.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());

        balanceService.deposit(depositDto, user);

        assertEquals(150.0, user.getBalance(), "Баланс пользователя должен быть обновлен");
        verify(currencyRepository, times(1)).findByCurrencyCode("USD");
        verify(userRepository, times(1)).save(user);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testDeposit_CurrencyNotFound() {
        when(currencyRepository.findByCurrencyCode("USD")).thenReturn(Optional.empty());

        CredentialNotFoundException exception = assertThrows(CredentialNotFoundException.class, () -> {
            balanceService.deposit(depositDto, user);
        });

        assertEquals("Currency not found", exception.getMessage(), "Ожидалось исключение 'Currency not found'");
        verify(currencyRepository, times(1)).findByCurrencyCode("USD");
        verify(userRepository, never()).save(any(User.class));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testDeposit_WithDifferentExchangeRate() {
        currency.setExchangeRate(1.5);
        when(currencyRepository.findByCurrencyCode("USD")).thenReturn(Optional.of(currency));
        when(userRepository.findUserWithCredentialsById(any(Long.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());

        balanceService.deposit(depositDto, user);

        assertEquals(175.0, user.getBalance(), "Баланс пользователя должен быть обновлен с учетом курса обмена");
        verify(currencyRepository, times(1)).findByCurrencyCode("USD");
        verify(userRepository, times(1)).save(user);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
}

