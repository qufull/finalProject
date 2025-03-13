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
import com.example.demo.dto.AvailableCarDto;
import com.example.demo.dto.EndReservationDto;
import com.example.demo.model.User;
import com.example.demo.service.CarService;
import com.example.demo.service.ReservationService;
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
public class ReservationControllerTest {
    @Mock
    private CarService carService;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
        objectMapper = new ObjectMapper();
    }

    // Тест для метода getAvailableCars
    @Test
    public void testGetAvailableCars() throws Exception {
        // Подготовка данных
        Long rentalPointId = 1L;
        AvailableCarDto availableCarDto = new AvailableCarDto();
        availableCarDto.setModel("Tesla Model S");

        List<AvailableCarDto> availableCars = Collections.singletonList(availableCarDto);

        // Мокирование сервиса
        when(carService.getAvailableCars(rentalPointId)).thenReturn(availableCars);

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/reservation/{rentalPointId}", rentalPointId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].model").value("Tesla Model S"));
    }

    // Тест для метода startReservation
    @Test
    public void testStartReservation() throws Exception {
        // Подготовка данных
        Long rentalPointId = 1L;
        Long carId = 1L;

        User user = new User();
        user.setId(1L);

        // Мокирование сервиса
        doNothing().when(reservationService).startReservation(any(User.class), anyLong(), anyLong());

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/reservation/{rentalPointId}/{carId}", rentalPointId, carId)
                        .contentType(MediaType.APPLICATION_JSON)) // Аутентификация
                .andExpect(status().isOk());
    }

    // Тест для метода endReservation
    @Test
    public void testEndReservation() throws Exception {
        // Подготовка данных
        Long rentalPointId = 1L;

        User user = new User();
        user.setId(1L);

        EndReservationDto endReservationDto = new EndReservationDto();
        endReservationDto.setTotalCost(100.0);

        // Мокирование сервиса
        when(reservationService.endReservation(any(User.class), anyLong())).thenReturn(endReservationDto);

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/reservation/end/{rentalPointId}", rentalPointId)
                        .contentType(MediaType.APPLICATION_JSON)) // Аутентификация
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost").value(100.0));
    }
}
