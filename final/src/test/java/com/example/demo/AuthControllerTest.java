package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.Instant;

import com.example.demo.controller.AuthController;
import com.example.demo.dto.DriverLicensDto;
import com.example.demo.dto.JwtAuthenticationResponse;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.model.User;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.DriverLicenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private DriverLicenseService driverLicenseService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    // Тест для успешной регистрации (signUp)
    @Test
    public void testSignUp_Success() throws Exception {
        // Подготовка данных
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("John");
        signUpRequest.setLastName("Doe");
        signUpRequest.setPhoneNumber("+1234567890");
        signUpRequest.setUsername("johndoe");
        signUpRequest.setEmail("john.doe@example.com");
        signUpRequest.setPassword("password123");

        JwtAuthenticationResponse response = new JwtAuthenticationResponse("token");

        // Мокирование сервиса
        when(authenticationService.signUp(any(SignUpRequest.class))).thenReturn(response);

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    // Тест для ошибки валидации при регистрации (signUp)
    @Test
    public void testSignUp_ValidationError() throws Exception {
        // Подготовка невалидных данных
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName(""); // Пустое имя
        signUpRequest.setLastName(""); // Пустая фамилия
        signUpRequest.setPhoneNumber("123"); // Невалидный номер телефона
        signUpRequest.setUsername("jd"); // Слишком короткое имя пользователя
        signUpRequest.setEmail("invalid-email"); // Невалидный email
        signUpRequest.setPassword("123"); // Слишком короткий пароль

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest());
    }

    // Тест для успешного входа (signIn)
    @Test
    public void testSignIn_Success() throws Exception {
        // Подготовка данных
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername("johndoe");
        signInRequest.setPassword("password123");

        JwtAuthenticationResponse response = new JwtAuthenticationResponse("token");

        // Мокирование сервиса
        when(authenticationService.signIn(any(SignInRequest.class))).thenReturn(response);

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    // Тест для ошибки валидации при входе (signIn)
    @Test
    public void testSignIn_ValidationError() throws Exception {
        // Подготовка невалидных данных
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername(""); // Пустое имя пользователя
        signInRequest.setPassword(""); // Пустой пароль

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isBadRequest());
    }

    // Тест для успешного добавления водительского удостоверения (addDriverLicense)
    @Test
    public void testAddDriverLicense_Success() throws Exception {
        DriverLicensDto driverLicensDto = new DriverLicensDto();
        driverLicensDto.setNumber("123456789");
        driverLicensDto.setDataOfIssue(Timestamp.from(Instant.now().minusSeconds(86400))); // Вчера
        driverLicensDto.setDataOfExpity(Timestamp.from(Instant.now().plusSeconds(86400 * 365))); // Через год

        // Мокирование сервиса
        when(driverLicenseService.createDriverLicense(any(DriverLicensDto.class), any(User.class))).thenReturn(driverLicensDto);

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/sign-up/driverlicense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverLicensDto))
                        .with(SecurityMockMvcRequestPostProcessors.user("johndoe").password("password123"))) // Аутентификация
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("123456789"));
    }

    // Тест для ошибки валидации при добавлении водительского удостоверения (addDriverLicense)
    @Test
    public void testAddDriverLicense_ValidationError() throws Exception {
        // Подготовка невалидных данных
        DriverLicensDto driverLicensDto = new DriverLicensDto();
        driverLicensDto.setNumber(""); // Пустой номер
        driverLicensDto.setDataOfIssue(null); // Отсутствует дата выдачи
        driverLicensDto.setDataOfExpity(null); // Отсутствует дата истечения срока действия

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/sign-up/driverlicense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverLicensDto)))
                .andExpect(status().isBadRequest());
    }
}
