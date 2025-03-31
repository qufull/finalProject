package com.example.demo.controller;

import com.example.demo.dto.AvailableCarDto;
import com.example.demo.dto.EndReservationDto;
import com.example.demo.dto.StartReservationDto;
import com.example.demo.exception.ReservationException;
import com.example.demo.model.User;
import com.example.demo.service.CarService;
import com.example.demo.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.ast.tree.expression.Star;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final CarService carService;
    private final ReservationService reservationService;

    @GetMapping("/{rentalPointId}")
    public List<AvailableCarDto> getAvailableCars(@PathVariable Long rentalPointId) {
        return carService.getAvailableCars(rentalPointId);
    }

    @PostMapping("/{rentalPointId}/{carId}")
    public StartReservationDto startReservation(@AuthenticationPrincipal User user, @PathVariable Long rentalPointId, @PathVariable Long carId) {
        return reservationService.startReservation(user, carId, rentalPointId);
    }

    @PostMapping("/end/{rentalPointId}")
    public EndReservationDto endReservation(@AuthenticationPrincipal User user, @PathVariable Long rentalPointId) {
        return reservationService.endReservation(user,rentalPointId);
    }
}

