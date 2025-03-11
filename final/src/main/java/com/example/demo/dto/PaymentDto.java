package com.example.demo.dto;

import com.example.demo.enums.PaymentStatus;
import com.example.demo.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    @NotNull(message = "Сумма не может быть null")
    @Positive(message = "Сумма должна быть положительной")
    private double amount;

    @NotNull(message = "Дата платежа не может быть null")
    private Timestamp paymentDate;

    @NotNull(message = "Тип платежа не может быть null")
    private PaymentType paymentType;

    @NotNull(message = "Статус платежа не может быть null")
    private PaymentStatus status;
}
