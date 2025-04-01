package com.example.demo.controller;


import com.example.demo.dto.PenaltiesDto;
import com.example.demo.dto.PenaltyDto;
import com.example.demo.service.PenaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/penalty")
public class AdminPenaltyController {

    private final PenaltyService penaltyService;

    @GetMapping
    public List<PenaltiesDto> getPenalties() {
        return penaltyService.getPenalties();
    }

    @PostMapping("/{carId}")
    public PenaltyDto createPenalty(@PathVariable Long carId, @RequestBody PenaltyDto penaltyDto) {
        return penaltyService.createPenalty(carId, penaltyDto);
    }
}
