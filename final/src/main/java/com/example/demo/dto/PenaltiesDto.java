package com.example.demo.dto;

import com.example.demo.enums.CarTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PenaltiesDto {
    @NotBlank(message = "Марка обязательна")
    private String make;

    @NotBlank(message = "Модель обязательна")
    private String model;

    @NotBlank(message = "Год выпуска обязателен")
    @Pattern(regexp = "^\\d{4}$", message = "Год выпуска должен быть в формате YYYY")
    private String year;

    @NotNull(message = "Тип транспортного средства не может быть null")
    private CarTypes type;

    @NotNull(message = "Сумма не может быть null")
    @Positive(message = "Сумма должна быть положительной")
    private double amount;

    @NotBlank(message = "Причина обязательна")
    private String reason;

    @NotNull(message = "Дата выдачи не может быть null")
    private Timestamp dateIssued;
}