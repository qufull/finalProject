package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndReservationDto {
    @NotNull(message = "Общая стоимость не может быть null")
    @Positive(message = "Общая стоимость должна быть положительной")
    private double totalCost;
}
