package com.example.demo.controller;

import com.example.demo.dto.AvailableCarDto;
import com.example.demo.dto.UpdateCarDto;
import com.example.demo.exception.CarException;
import com.example.demo.service.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/admin/cars")
public class AdminCarController {

    private final CarService carService;

    @GetMapping
    public List<AvailableCarDto> getCars() {
        log.info("Received request to get all cars");
        try {
            List<AvailableCarDto> cars = carService.getAll();
            log.info("Successfully retrieved {} cars", cars.size());
            return cars;
        } catch (Exception e) {
            log.error("Error while retrieving cars", e);
            throw new CarException("Failed to retrieve cars");
        }
    }

    @PostMapping
    public AvailableCarDto createCar(@RequestBody AvailableCarDto carDto) {
        log.info("Received request to create a new car with details: {}", carDto);
        try {
            AvailableCarDto createdCar = carService.create(carDto);
            log.info("Successfully created car");
            return createdCar;
        } catch (Exception e) {
            log.error("Error while creating a new car", e);
            throw new CarException("Failed to create car");
        }
    }

    @PostMapping("/{id}")
    public AvailableCarDto updateCar(@PathVariable Long id, @RequestBody UpdateCarDto carDto) {
        log.info("Received request to update car with id: {}", id);
        try {
            AvailableCarDto updatedCar = carService.update(id, carDto);
            log.info("Successfully updated car");
            return updatedCar;
        } catch (Exception e) {
            log.error("Error while updating car with id: {}", id, e);
            throw new CarException("Failed to update car");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id) {
        log.info("Received request to delete car with id: {}", id);
        try {
            carService.delete(id);
            log.info("Successfully deleted car with id: {}", id);
        } catch (Exception e) {
            log.error("Error while deleting car with id: {}", id, e);
            throw new CarException("Failed to delete car");
        }
    }
}
