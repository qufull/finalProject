package com.example.demo.controller;

import com.example.demo.dto.AvailableCarDto;
import com.example.demo.dto.CarDto;
import com.example.demo.dto.UpdateCarDto;
import com.example.demo.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cars")
public class AdminCarController {

    private final CarService carService;

    @GetMapping
    public List<AvailableCarDto> getCars() {
        return carService.getAll();
    }

    @PostMapping
    public AvailableCarDto createCar(@RequestBody AvailableCarDto carDto) {
        return carService.create(carDto);
    }

    @PostMapping("/{id}")
    public AvailableCarDto updateCar(@PathVariable Long id, @RequestBody UpdateCarDto carDto) {
        return carService.update(id, carDto);
    }

    @DeleteMapping("/{id}")
    public CarDto deleteCar(@PathVariable Long id) {
        return carService.delete(id);
    }
}

