package com.example.demo.service;

import com.example.demo.dto.AvailableCarDto;
import com.example.demo.dto.CarDto;
import com.example.demo.dto.UpdateCarDto;
import com.example.demo.exception.CarNotFoundException;
import com.example.demo.mapper.CarMapper;
import com.example.demo.model.Car;
import com.example.demo.model.enums.CarStatus;
import com.example.demo.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public CarDto delete(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car not found"));
        carRepository.deleteById(id);
        return carMapper.toCarDto(car);
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
