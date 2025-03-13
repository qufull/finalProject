package com.example.demo.controller;

import com.example.demo.dto.DriverLicensDto;
import com.example.demo.dto.JwtAuthenticationResponse;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.model.User;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.DriverLicenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final DriverLicenseService driverLicenseService;

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }

    @PostMapping("/sign-up/driverlicense")
    public DriverLicensDto addDriverLicense(@RequestBody @Valid DriverLicensDto driverLicensDto, @AuthenticationPrincipal User user) {
        return driverLicenseService.createDriverLicense(driverLicensDto, user);
    }
}