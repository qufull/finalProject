package com.example.demo.service;

import com.example.demo.dto.JwtAuthenticationResponse;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.enums.UserRoles;
import com.example.demo.enums.UserStatus;
import com.example.demo.exception.UserCreateException;
import com.example.demo.exception.UserException;
import com.example.demo.model.Credential;
import com.example.demo.model.User;
import com.example.demo.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Transactional
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        log.info("Starting sign-up process for user with username: {}", request.getUsername());
        try{
        User user = User.builder()
                .status(UserStatus.ACTIVE)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .roles(List.of(roleService.findByName(UserRoles.USER)))
                .registrationDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        Credential credential = Credential.builder()
                .username(request.getUsername())
                .user(user)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        user.setCredential(credential);

        userService.create(user);

        String jwt = jwtUtil.generateToken(user);
        log.info("User {} successfully signed up and JWT token generated", request.getUsername());
        return new JwtAuthenticationResponse(jwt);
    } catch (UserCreateException e){
            log.error("Error during sign-up process for user with username: {}", request.getUsername());
            throw new UserCreateException("Failed to sign up user");
        }
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        log.info("Starting sign-in process for user with username: {}", request.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    ));

            User user = (User) authentication.getPrincipal();

            if (!user.getStatus().equals(UserStatus.ACTIVE)) {
                log.warn("User {} is blocked and cannot sign in", request.getUsername());
                throw new UserException("User is Blocked");
            }
            String jwt = jwtUtil.generateToken(user);
            log.info("User {} successfully signed in and JWT token generated", request.getUsername());
            return new JwtAuthenticationResponse(jwt);
        } catch (BadCredentialsException e) {
            log.error("Invalid username or password for user: {}", request.getUsername(), e);
            throw new UserException("Invalid username or password");
        }
    }
}
