package com.example.demo.service;

import com.example.demo.dto.AvailableCarDto;
import com.example.demo.dto.UpdateCarDto;
import com.example.demo.enums.CarStatus;
import com.example.demo.exception.CarException;
import com.example.demo.exception.CarNotFoundException;
import com.example.demo.mapper.CarMapper;
import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public AvailableCarDto create (AvailableCarDto availableCarDto) {

        log.info("Creating a new car with details: {}", availableCarDto);
        try {
            Car car = carMapper.dtoToCar(availableCarDto);
            Car savedCar = carRepository.save(car);
            log.info("Car created successfully with id: {}", savedCar.getId());

            return carMapper.toDto(savedCar);
        } catch (CarException e) {
            log.error("Error creating a new car");
            throw new CarException("Failed to create car");
        }
    }

    public AvailableCarDto update(Long id, UpdateCarDto updateCarDto) {
        log.info("Updating car with id: {}", id);

        try {
            Car car = carRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Car not found with id: {}", id);
                        return new CarNotFoundException("Car not found");
                    });

            carMapper.updateCarFromUpdateCar(updateCarDto, car);
            Car updatedCar = carRepository.save(car);
            log.info("Car updated successfully with id: {}", updatedCar.getId());

            return carMapper.toDto(updatedCar);
        } catch (CarNotFoundException e) {
            log.error("Car not found with id: {}", id, e);
            throw e;
        } catch (CarException e) {
            log.error("Error updating car with id: {}", id);
            throw new CarException("Failed to update car");
        }
    }

    public void delete(Long id) {
        log.info("Deleting car with id: {}", id);

        try {
            carRepository.deleteById(id);
            log.info("Car deleted successfully with id: {}", id);
        } catch (CarException e) {
            log.error("Error deleting car with id: {}", id);
            throw new CarException("Failed to delete car");
        }
    }

    public List<AvailableCarDto> getAvailableCars() {
        log.info("Fetching all available cars");

        try {
            List<Car> availableCars = carRepository.findAllByStatus(CarStatus.AVAILABLE)
                    .orElseThrow(() -> {
                        log.error("No available cars found");
                        return new CarNotFoundException("Cars not found");
                    });

            log.info("Found {} available cars", availableCars.size());
            return carMapper.toDtoList(availableCars);
        } catch (CarNotFoundException e) {
            log.error("No available cars found", e);
            throw e;
        } catch (CarException e) {
            log.error("Error fetching available cars", e);
            throw new CarException("Failed to fetch available cars");
        }
    }

    public List<AvailableCarDto> getAll() {
        log.info("Fetching all cars");

        try {
            List<Car> allCars = carRepository.findAll();
            log.info("Found {} cars", allCars.size());

            return carMapper.toDtoList(allCars);
        } catch (CarException e) {
            log.error("Error fetching all cars", e);
            throw new CarException("Failed to fetch all cars");
        }
    }
}
