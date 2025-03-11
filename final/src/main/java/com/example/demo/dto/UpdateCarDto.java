package com.example.demo.dto;


import com.example.demo.enums.CarStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarDto {

    @NotNull(message = "Цена за минуту не может быть null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена за минуту должна быть больше 0")
    private double pricePerMinute;

    @NotNull(message = "Статус автомобиля не может быть null")
    private CarStatus status;
}
