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

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableCarDto {

    @NotBlank(message = "Марка обязательна")
    private String make;

    @NotBlank(message = "Модель обязательна")
    private String model;

    @NotBlank(message = "Год выпуска обязателен")
    @Pattern(regexp = "^\\d{4}$", message = "Год выпуска должен быть в формате YYYY")
    private String year;

    @NotNull(message = "Цена за минуту не может быть null")
    @Positive(message = "Цена за минуту должна быть положительной")
    private double pricePerMinute;

    @NotNull(message = "Статус автомобиля не может быть null")
    private CarStatus status;

    @NotNull(message = "Тип автомобиля не может быть null")
    private CarTypes type;

}
