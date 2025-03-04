package com.example.demo.service;

import com.example.demo.dto.DepositDto;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.enums.PaymentType;
import com.example.demo.model.Currency;
import com.example.demo.model.Payment;
import com.example.demo.model.User;
import com.example.demo.repository.CurrencyRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void deposit(DepositDto dto,User user) {
        Currency currency = currencyRepository.findByCurrencyCode(dto.getCurrencyCode())
                .orElseThrow(() -> new RuntimeException("Currency not found"));

        double convertedAmount = dto.getAmount() * currency.getExchangeRate();

        user.setBalance(user.getBalance() + convertedAmount);
        userRepository.save(user);

        Payment payment = Payment.builder()
                .user(user)
                .currency(currency)
                .amount(dto.getAmount())
                .paymentType(PaymentType.REPLENISHMENT)
                .status(PaymentStatus.APPROVED)
                .paymentDate(new Timestamp(System.currentTimeMillis()))
                .build();

        paymentRepository.save(payment);
    }
}
