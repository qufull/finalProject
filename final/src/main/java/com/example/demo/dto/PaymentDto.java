package com.example.demo.dto;

import com.example.demo.model.enums.PaymentStatus;
import com.example.demo.model.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
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
