package com.example.demo.dto;

import com.example.demo.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusDto {
    @NotNull(message = "Статус пользователя не может быть null")
    private UserStatus status;
}
