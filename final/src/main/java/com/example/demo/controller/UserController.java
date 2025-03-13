package com.example.demo.controller;

import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.dto.DepositDto;
import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.UpdateUser;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.CredentialException;
import com.example.demo.exception.UserException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.service.BalanceService;
import com.example.demo.service.CredentialService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/profile")
public class UserController {
    private final UserService userService;
    private final BalanceService balanceService;
    private final PaymentService paymentService;

    @GetMapping
    public UserDto getProfile(@AuthenticationPrincipal User user) {
        return userService.getUser(user);
    }

    @PostMapping
    public UserDto updateProfile(@RequestBody UpdateUser userDto ,@AuthenticationPrincipal User user) {
        return userService.updateUser(userDto,user);
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordDto changePasswordDto,@AuthenticationPrincipal User user) {
        userService.changePassword(changePasswordDto, user);
    }

    @PostMapping("/deposit")
    public void deposit(@RequestBody DepositDto depositDto,@AuthenticationPrincipal User user) {
        balanceService.deposit(depositDto, user);
    }

    @GetMapping("/payments")
    public List<PaymentDto> getPayments(@AuthenticationPrincipal User user) {
        return paymentService.findAllByUserId(user);
    }
}
