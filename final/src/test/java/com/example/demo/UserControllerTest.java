package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Collections;
import java.util.List;

import com.example.demo.controller.ReservationController;
import com.example.demo.controller.UserController;
import com.example.demo.dto.AvailableCarDto;
import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.dto.DepositDto;
import com.example.demo.dto.EndReservationDto;
import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.UpdateUser;
import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.service.BalanceService;
import com.example.demo.service.CarService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ReservationService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BalanceService balanceService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetProfile() throws Exception {
        User user = new User();
        user.setId(1L);

        UserDto userDto = new UserDto();
        userDto.setUsername("johndoe");

        when(userService.getUser(any(User.class))).thenReturn(userDto);

        mockMvc.perform(get("/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johndoe"));
    }

    @Test
    public void testUpdateProfile() throws Exception {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setUsername("johndoe_updated");

        User user = new User();
        user.setId(1L);

        UserDto userDto = new UserDto();
        userDto.setUsername("johndoe_updated");

        when(userService.updateUser(any(UpdateUser.class), any(User.class))).thenReturn(userDto);

        mockMvc.perform(post("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johndoe_updated"));
    }

    @Test
    public void testChangePassword() throws Exception {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setPassword("newPassword");

        User user = new User();
        user.setId(1L);

        doNothing().when(userService).changePassword(any(ChangePasswordDto.class), any(User.class));

        mockMvc.perform(post("/profile/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeposit() throws Exception {
        DepositDto depositDto = new DepositDto();
        depositDto.setAmount(100.0);

        User user = new User();
        user.setId(1L);

        doNothing().when(balanceService).deposit(any(DepositDto.class), any(User.class));


        mockMvc.perform(post("/profile/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPayments() throws Exception {
        User user = new User();
        user.setId(1L);

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setAmount(100.0);

        List<PaymentDto> payments = Collections.singletonList(paymentDto);

        when(paymentService.findAllByUserId(any(User.class))).thenReturn(payments);

        mockMvc.perform(get("/profile/payments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(100.0));
    }
}
