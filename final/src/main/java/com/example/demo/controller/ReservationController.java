package com.example.demo.controller;

import com.example.demo.dto.AvailableCarDto;
import com.example.demo.dto.EndReservationDto;
import com.example.demo.exception.ReservationException;
import com.example.demo.model.User;
import com.example.demo.service.CarService;
import com.example.demo.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final CarService carService;
    private final ReservationService reservationService;

    @GetMapping
    public List<AvailableCarDto> getAvailableCars() {
        log.info("Fetching available cars");
        List<AvailableCarDto> availableCars = carService.getAvailableCars();
        log.info("Fetched {} available cars", availableCars.size());
        return availableCars;
    }

    @PostMapping("/{carId}")
    public void startReservation(@AuthenticationPrincipal User user, @PathVariable Long carId) {
        log.info("User {} is attempting to start a reservation for car ID: {}", user.getId(), carId);
        try {
            reservationService.startReservation(user, carId);
            log.info("Successfully started reservation for user {} and car ID: {}", user.getId(), carId);
        } catch (Exception e) {
            log.error("Error while starting reservation for user {} and car ID: {}", user.getId(), carId, e);
            throw new ReservationException("Failed to start reservation");
        }
    }

    @PostMapping("/end")
    public EndReservationDto endReservation(@AuthenticationPrincipal User user) {
        log.info("User {} is attempting to end their reservation", user.getId());
        EndReservationDto endReservationDto = reservationService.endReservation(user);
        log.info("Successfully ended reservation for user {}", user.getId());
        return endReservationDto;
    }
}

