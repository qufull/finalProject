package com.example.demo.service;

import com.example.demo.dto.PaymentDto;
import com.example.demo.exception.PaymentException;
import com.example.demo.exception.PaymentNotFoundException;
import com.example.demo.mapper.PaymentMapper;
import com.example.demo.model.User;
import com.example.demo.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public List<PaymentDto> findAllByUserId(User user) {
        Long userId = user.getId();
        log.info("Fetching payments for user with id: {}", userId);

        try {
            return paymentMapper.toDtoList(paymentRepository.findByUserId(userId)
                    .orElseThrow(() -> {
                        log.error("Payments not found for user id: {}", userId);
                        return new PaymentNotFoundException("Payments not found for user with id: " + userId);
                    }));
        } catch (PaymentNotFoundException e) {
            log.error("Error fetching payments: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching payments for user id: {}", userId, e);
            throw new PaymentException("Failed to fetch payments");
        }
    }
}
