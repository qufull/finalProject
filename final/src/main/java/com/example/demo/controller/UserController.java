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
        log.info("Fetching profile for user ID: {}", user.getId());
        UserDto userDto = userService.getUser(user);
        log.info("Successfully fetched profile for user ID: {}", user.getId());
        return userDto;
    }

    @PostMapping
    public UserDto updateProfile(@RequestBody UpdateUser userDto ,@AuthenticationPrincipal User user) {
        log.info("Updating profile for user ID: {}", user.getId());
        UserDto updatedUser = userService.updateUser(userDto,user);
        log.info("Successfully updated profile for user ID: {}", user.getId());
        return updatedUser;
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordDto changePasswordDto,@AuthenticationPrincipal User user) {
        log.info("Changing password for user ID: {}", user.getId());
        try {
            userService.changePassword(changePasswordDto, user);
            log.info("Successfully changed password for user ID: {}", user.getId());
        } catch (Exception e) {
            log.error("Error while changing password for user ID: {}", user.getId(), e);
            throw new UserException("Failed to change password");
        }
    }

    @PostMapping("/deposit")
    public void deposit(@RequestBody DepositDto depositDto,@AuthenticationPrincipal User user) {
        log.info("Depositing amount for user ID: {}. Amount: {}", user.getId(), depositDto.getAmount());
        try {
            balanceService.deposit(depositDto, user);
            log.info("Successfully deposited amount for user ID: {}. Amount: {}", user.getId(), depositDto.getAmount());
        } catch (Exception e) {
            log.error("Error while depositing amount for user ID: {}", user.getId(), e);
            throw new CredentialException("Failed to deposit amount");
        }
    }

    @GetMapping("/payments")
    public List<PaymentDto> getPayments(@AuthenticationPrincipal User user) {
        log.info("Fetching payments for user ID: {}", user.getId());
        List<PaymentDto> payments = paymentService.findAllByUserId(user);
        log.info("Fetched {} payments for user ID: {}", payments.size(), user.getId());
        return payments;
    }
}
