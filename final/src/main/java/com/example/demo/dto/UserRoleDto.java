package com.example.demo.dto;

import com.example.demo.enums.UserRoles;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDto {
    @NotNull(message = "Список ролей не может быть null")
    @NotEmpty(message = "Список ролей не может быть пустым")
    private List<UserRoles> role;

}
