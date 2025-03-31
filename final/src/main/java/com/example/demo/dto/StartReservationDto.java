package com.example.demo.dto;

import com.example.demo.model.enums.CarTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
