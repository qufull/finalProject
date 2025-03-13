package com.example.demo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import com.example.demo.controller.AdminCarController;
import com.example.demo.dto.AvailableCarDto;
import com.example.demo.dto.UpdateCarDto;
import com.example.demo.service.CarService;
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
public class AdminCarControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarService carService;

    @InjectMocks
    private AdminCarController adminCarController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminCarController).build();
    }

    @Test
    public void testGetCars() throws Exception {
        AvailableCarDto car1 = new AvailableCarDto();
        AvailableCarDto car2 = new AvailableCarDto();
        List<AvailableCarDto> cars = Arrays.asList(car1, car2);

        when(carService.getAll()).thenReturn(cars);

        mockMvc.perform(get("/admin/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists());

        verify(carService, times(1)).getAll();
    }

    @Test
    public void testCreateCar() throws Exception {
        AvailableCarDto carDto = new AvailableCarDto();
        AvailableCarDto createdCar = new AvailableCarDto();

        when(carService.create(any(AvailableCarDto.class))).thenReturn(createdCar);

        mockMvc.perform(post("/admin/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(carService, times(1)).create(any(AvailableCarDto.class));
    }

    @Test
    public void testUpdateCar() throws Exception {
        Long id = 1L;
        UpdateCarDto updateCarDto = new UpdateCarDto();
        AvailableCarDto updatedCar = new AvailableCarDto();

        when(carService.update(anyLong(), any(UpdateCarDto.class))).thenReturn(updatedCar);

        mockMvc.perform(post("/admin/cars/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCarDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(carService, times(1)).update(anyLong(), any(UpdateCarDto.class));
    }

    @Test
    public void testDeleteCar() throws Exception {
        Long id = 1L;

        doNothing().when(carService).delete(anyLong());

        mockMvc.perform(delete("/admin/cars/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(carService, times(1)).delete(anyLong());
    }
}