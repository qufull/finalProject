package com.example.demo.service;

import com.example.demo.dto.PaymentDto;
import com.example.demo.mapper.PaymentMapper;
import com.example.demo.model.Payment;
import com.example.demo.model.User;
import com.example.demo.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public List<PaymentDto> findAllByUserId(User user) {
        Long userId = user.getId();
        return paymentMapper.toDtoList(paymentRepository.findByUserId(userId).orElseThrow(()
                -> new RuntimeException("payments not foundd")));
    }

}
