package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PenaltyDto {
    @NotNull(message = "Сумма не может быть null")
    @Positive(message = "Сумма должна быть положительной")
    private double amount;

    @NotBlank(message = "Причина обязательна")
    private String reason;
}