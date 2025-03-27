package com.example.demo.dto;

import com.example.demo.enums.CarStatus;
import com.example.demo.enums.CarTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartReservationDto {

    @NotBlank(message = "Марка обязательна")
    private String make;

    @NotBlank(message = "Модель обязательна")
    private String model;

    @NotBlank(message = "Год выпуска обязателен")
    @Pattern(regexp = "^\\d{4}$", message = "Год выпуска должен быть в формате YYYY")
    private String year;

    @NotNull(message = "Тип автомобиля не может быть null")
    private CarTypes type;

    @NotNull(message = "Дата начала аренды не может быть null")
    private Timestamp startTime;
}
