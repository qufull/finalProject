package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import com.example.demo.controller.AdminPenaltyController;
import com.example.demo.dto.PenaltiesDto;
import com.example.demo.dto.PenaltyDto;
import com.example.demo.service.PenaltyService;
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
public class AdminPenaltyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PenaltyService penaltyService;

    @InjectMocks
    private AdminPenaltyController adminPenaltyController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminPenaltyController).build();
    }

    @Test
    public void testGetPenalties() throws Exception {
        PenaltiesDto penalty1 = new PenaltiesDto();
        PenaltiesDto penalty2 = new PenaltiesDto();
        List<PenaltiesDto> penalties = Arrays.asList(penalty1, penalty2);

        when(penaltyService.getPenalties()).thenReturn(penalties);

        mockMvc.perform(get("/admin/penalty"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(penaltyService, times(1)).getPenalties();
    }

    @Test
    public void testCreatePenalty() throws Exception {
        Long carId = 1L;
        PenaltyDto penaltyDto = new PenaltyDto();

        doNothing().when(penaltyService).createPenalty(anyLong(), any(PenaltyDto.class));

        mockMvc.perform(post("/admin/penalty/{carId}", carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(penaltyDto)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(penaltyService, times(1)).createPenalty(anyLong(), any(PenaltyDto.class));
    }
}