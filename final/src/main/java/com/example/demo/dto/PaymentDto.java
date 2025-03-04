package com.example.demo.dto;

import com.example.demo.enums.PaymentStatus;
import com.example.demo.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private double amount;
    private Timestamp paymentDate;
    private PaymentType paymentType;
    private PaymentStatus status;
}
