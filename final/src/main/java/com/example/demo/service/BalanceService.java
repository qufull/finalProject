package com.example.demo.service;

import com.example.demo.dto.DepositDto;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.enums.PaymentType;
import com.example.demo.exception.CredentialNotFoundException;
import com.example.demo.model.Currency;
import com.example.demo.model.Payment;
import com.example.demo.model.User;
import com.example.demo.repository.CurrencyRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceService {
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void deposit(DepositDto dto,User user) {
            Currency currency = currencyRepository.findByCurrencyCode(dto.getCurrencyCode())
                    .orElseThrow(() ->
                    {
                        log.error("Currency not found for code: {}", dto.getCurrencyCode());
                        return new CredentialNotFoundException("Currency not found");
                    });

            double convertedAmount = dto.getAmount() * currency.getExchangeRate();

            double newBalance = user.getBalance() + convertedAmount;
            user.setBalance(newBalance);
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
