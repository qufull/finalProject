package com.example.demo.dto;

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
public class UserDto {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    @NotBlank(message = "Имя не может быть пустым")
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой")
    private String lastName;

    @NotNull(message = "Баланс не может быть null")
    @Positive(message = "Баланс должен быть положительным")
    private double balance;

    @NotBlank(message = "Валюта не может быть пустой")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Валюта должна быть в формате ISO 4217 (например, USD)")
    private String currency;
}
