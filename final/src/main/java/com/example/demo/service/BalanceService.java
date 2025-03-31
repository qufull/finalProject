package com.example.demo.service;

import com.example.demo.dto.DepositDto;
import com.example.demo.model.enums.PaymentStatus;
import com.example.demo.model.enums.PaymentType;
import com.example.demo.exception.CredentialNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.PaymentMapper;
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

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    public DepositDto deposit(DepositDto dto,User user) {
            Currency currency = currencyRepository.findByCurrencyCode(dto.getCurrencyCode())
                    .orElseThrow(() ->
                    {
                        return new CredentialNotFoundException("Currency not found");
                    });

            double convertedAmount = dto.getAmount() * currency.getExchangeRate();

            double newBalance = user.getBalance() + convertedAmount;

            User newUser = userRepository.findUserWithCredentialsById(user.getId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            newUser.setBalance(newBalance);
            userRepository.save(newUser);

            Payment payment = Payment.builder()
                    .user(user)
                    .currency(currency)
                    .amount(dto.getAmount())
                    .paymentType(PaymentType.REPLENISHMENT)
                    .status(PaymentStatus.APPROVED)
                    .paymentDate(new Timestamp(System.currentTimeMillis()))
                    .build();

            paymentRepository.save(payment);

            return paymentMapper.toDepositDto(convertedAmount,dto.getCurrencyCode());
    }
}
