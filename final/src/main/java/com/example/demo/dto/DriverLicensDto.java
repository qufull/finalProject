package com.example.demo.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverLicensDto {

    @NotBlank(message = "Номер водительского удостоверения обязателен")
    private String number;

    @NotNull(message = "Дата выдачи не может быть null")
    @Past(message = "Дата выдачи должна быть в прошлом")
    private Timestamp dataOfIssue;

    @NotNull(message = "Дата истечения срока действия не может быть null")
    @Future(message = "Дата истечения срока действия должна быть в будущем")
    private Timestamp dataOfExpity;
}
