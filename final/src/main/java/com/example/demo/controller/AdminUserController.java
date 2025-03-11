package com.example.demo.controller;

import com.example.demo.dto.AdminPanelUserDto;
import com.example.demo.dto.UserRoleDto;
import com.example.demo.dto.UserStatusDto;
import com.example.demo.enums.UserStatus;
import com.example.demo.exception.UserException;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public List<AdminPanelUserDto> getUsers(@AuthenticationPrincipal User user) {
        log.info("Admin {} is fetching all users", user.getId());
        List<AdminPanelUserDto> users = userService.getAdminPanelUsers(user.getId());
        log.info("Fetched {} users for admin {}", users.size(), user.getId());
        return users;
    }

    @PostMapping("/status/{id}")
    public void changeUserStatus(@PathVariable Long id, @RequestBody UserStatusDto dto) {
        log.info("Admin {} is changing status for user.", id);
        try {
            userService.updateUserStatus(id, dto);
            log.info("Successfully changed status for user with ID: {}", id);
        }catch (Exception e){
            log.error("Error while changing status for user with ID: {}", id, e);
            throw new UserException("Failed to change user status");
        }
    }

    @PostMapping("/roles/{id}")
    public void changeUserRoles(@PathVariable Long id, @RequestBody UserRoleDto dto){
        log.info("Admin is changing roles for user with ID: {}", id);
        try {
            userService.updateUserRoles(id, dto);
            log.info("Successfully changed roles for user with ID: {}", id);
        }catch (Exception e){
            log.error("Error while changing roles for user with ID: {}", id, e);
            throw new UserException("Failed to change user roles");
        }
    }
}
