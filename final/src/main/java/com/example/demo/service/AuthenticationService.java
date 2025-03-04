package com.example.demo.service;

import com.example.demo.dto.JwtAuthenticationResponse;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.enums.UserRoles;
import com.example.demo.enums.UserStatus;
import com.example.demo.model.Credential;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CredentialService credentialService;
    private final UserService userService;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {

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
        return new JwtAuthenticationResponse(jwt);
    }


    public JwtAuthenticationResponse signIn(SignInRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
        ));

        UserDetails user = (UserDetails) authentication.getPrincipal();

        String jwt = jwtUtil.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }



}
