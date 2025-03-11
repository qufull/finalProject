package com.example.demo;

import com.example.demo.dto.JwtAuthenticationResponse;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.enums.UserRoles;
import com.example.demo.enums.UserStatus;
import com.example.demo.exception.UserException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.CarService;
import com.example.demo.service.CredentialService;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest("user", "pass", "email@example.com", "John", "Doe", "1234567890");
        signInRequest = new SignInRequest("user", "pass");
    }

    @Test
    void testSignUp() {
        when(roleService.findByName(any())).thenReturn(new Role());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(any(User.class))).thenReturn("jwtToken");

        JwtAuthenticationResponse response = authenticationService.signUp(signUpRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(userService).create(any(User.class));
    }

    @Test
    void testSignIn() {
        User user = new User();
        user.setStatus(UserStatus.ACTIVE);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null));
        when(jwtUtil.generateToken(any(User.class))).thenReturn("jwtToken");

        JwtAuthenticationResponse response = authenticationService.signIn(signInRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
    }

    @Test
    void testSignIn_UserBlocked() {
        User user = new User();
        user.setStatus(UserStatus.BLOCKED);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null));

        Exception exception = assertThrows(UserException.class, () -> {
            authenticationService.signIn(signInRequest);
        });

        assertEquals("User is Blocked", exception.getMessage());
    }

    @Test
    void testSignIn_BadCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        Exception exception = assertThrows(UserException.class, () -> {
            authenticationService.signIn(signInRequest);
        });

        assertEquals("Invalid username or password", exception.getMessage());
    }
}
