package com.example.demo.dto;

import com.example.demo.model.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusDto {
    @NotNull(message = "Статус пользователя не может быть null")
    private UserStatus status;
}
