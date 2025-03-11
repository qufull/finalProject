package com.example.demo.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 3, max = 20, message = "Имя пользователя должно быть от 3 до 20 символов")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max = 30, message = "Пароль должен быть от 6 до 30 символов")
    private String password;
}
