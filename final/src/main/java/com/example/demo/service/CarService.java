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
        Car car = carMapper.dtoToCar(availableCarDto);
        Car savedCar = carRepository.save(car);
        return carMapper.toDto(savedCar);
    }

    public AvailableCarDto update(Long id, UpdateCarDto updateCarDto) {
        Car car = carRepository.findById(id)
                    .orElseThrow(() -> new CarNotFoundException("Car not found"));

        carMapper.updateCarFromUpdateCar(updateCarDto, car);
        Car updatedCar = carRepository.save(car);
        return carMapper.toDto(updatedCar);
    }

    public void delete(Long id) {
        carRepository.deleteById(id);
    }

    public List<AvailableCarDto> getAvailableCars(Long rentalPointId) {
        List<Car> availableCars = carRepository.findAllByStatusAndRentalPointId(CarStatus.AVAILABLE,rentalPointId)
                    .orElseThrow(() -> new CarNotFoundException("Cars not found"));
        return carMapper.toDtoList(availableCars);
    }

    public List<AvailableCarDto> getAll() {
        return carMapper.toDtoList(carRepository.findAll());
    }
}
