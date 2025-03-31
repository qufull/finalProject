package com.example.demo.controller;

import com.example.demo.dto.AdminPanelUserDto;
import com.example.demo.dto.UserRoleDto;
import com.example.demo.dto.UserStatusDto;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public List<AdminPanelUserDto> getUsers(@AuthenticationPrincipal User user) {
        return userService.getAdminPanelUsers(user.getId());
    }

    @PostMapping("/status/{id}")
    public void changeUserStatus(@PathVariable Long id, @RequestBody UserStatusDto dto) {
        userService.updateUserStatus(id, dto);
    }

    @PostMapping("/roles/{id}")
    public void changeUserRoles(@PathVariable Long id, @RequestBody UserRoleDto dto){
        userService.updateUserRoles(id, dto);
    }
}
