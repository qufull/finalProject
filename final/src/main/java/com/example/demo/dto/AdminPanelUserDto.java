package com.example.demo.dto;

import com.example.demo.model.enums.UserRoles;
import com.example.demo.model.enums.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminPanelUserDto {
    @NotNull(message = "ID пользователя не может быть null")
    private Long id;

    @NotBlank(message = "Имя пользователя обязательное")
    private String username;

    @NotBlank(message = "Имя обязательное")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @NotNull(message = "Баланс не может быть null")
    @Positive(message = "Баланс должен быть положительным")
    private double balance;

    @NotBlank(message = "Валюта обязательна")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Валюта должна быть в формате ISO 4217 (например, USD)")
    private String currency;

    @NotNull(message = "Статус пользователя не может быть null")
    private UserStatus status;

    @NotNull(message = "Список ролей не может быть null")
    @NotEmpty(message = "Список ролей не может быть пустым")
    private List<UserRoles> roles;
}
