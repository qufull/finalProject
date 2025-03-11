package com.example.demo.controller;


import com.example.demo.dto.PenaltiesDto;
import com.example.demo.dto.PenaltyDto;
import com.example.demo.exception.PenaltyException;
import com.example.demo.model.Penalty;
import com.example.demo.service.PenaltyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/penalty")
public class AdminPenaltyController {


    private final PenaltyService penaltyService;

    @GetMapping
    public List<PenaltiesDto> getPenalties() {
        log.info("Received request to get all penalties");
        try {
            List<PenaltiesDto> penalties = penaltyService.getPenalties();
            log.info("Successfully retrieved {} penalties", penalties.size());
            return penalties;
        } catch (Exception e) {
            log.error("Error while retrieving penalties", e);
            throw new PenaltyException("Failed to retrieve penalties");
        }
    }

    @PostMapping("/{carId}")
    public void createPenalty(@PathVariable Long carId, @RequestBody PenaltyDto penaltyDto) {
        log.info("Received request to create a penalty for car with id: {}", carId);
        try {
            penaltyService.createPenalty(carId, penaltyDto);
            log.info("Successfully created penalty for car with id: {}", carId);
        } catch (Exception e) {
            log.error("Error while creating penalty for car with id: {}", carId, e);
            throw new PenaltyException("Failed to create penalty");
        }
    }
}
