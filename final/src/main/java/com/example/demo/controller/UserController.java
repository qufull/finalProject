package com.example.demo.controller;

import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.dto.DepositDto;
import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.UpdateUser;
import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.service.BalanceService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public DepositDto deposit(@RequestBody DepositDto depositDto,@AuthenticationPrincipal User user) {
        return balanceService.deposit(depositDto, user);
    }

    @GetMapping("/payments")
    public List<PaymentDto> getPayments(@AuthenticationPrincipal User user) {
        return paymentService.findAllByUserId(user);
    }
}
