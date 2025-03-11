package com.example.demo.dto;

import com.example.demo.model.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositDto {
    @NotNull(message = "Сумма не может быть null")
    @Positive(message = "Сумма должна быть положительной")
    private double amount;

    @NotBlank(message = "Код валюты обязателен")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Код валюты должен быть в формате ISO 4217 (например, USD)")
    private String currencyCode;
}
