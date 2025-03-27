package com.example.demo;

import com.example.demo.dto.AvailableCarDto;
import com.example.demo.dto.UpdateCarDto;
import com.example.demo.enums.CarStatus;
import com.example.demo.exception.CarException;
import com.example.demo.exception.CarNotFoundException;
import com.example.demo.mapper.CarMapper;
import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;
import com.example.demo.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarService carService;

    private AvailableCarDto availableCarDto;
    private UpdateCarDto updateCarDto;
    private Car car;

    @BeforeEach
    void setUp() {
        availableCarDto = new AvailableCarDto();
        availableCarDto.setMake("Toyota");
        availableCarDto.setModel("Corolla");

        updateCarDto = new UpdateCarDto();
        updateCarDto.setPricePerMinute(1.5);
        updateCarDto.setStatus(CarStatus.AVAILABLE);

        car = new Car();
        car.setId(1L);
        car.setMake("Toyota");
        car.setModel("Corolla");
        car.setStatus(CarStatus.AVAILABLE);
    }

    @Test
    void testCreateCar_Success() {
        when(carMapper.dtoToCar(any(AvailableCarDto.class))).thenReturn(car);
        when(carRepository.save(any(Car.class))).thenReturn(car);
        when(carMapper.toDto(any(Car.class))).thenReturn(availableCarDto);

        AvailableCarDto result = carService.create(availableCarDto);

        assertNotNull(result);
        assertEquals("Toyota", result.getMake());
        assertEquals("Corolla", result.getModel());
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void testCreateCar_Exception() {
        when(carMapper.dtoToCar(any(AvailableCarDto.class))).thenThrow(new CarException("Failed to create car"));

        CarException exception = assertThrows(CarException.class, () -> {
            carService.create(availableCarDto);
        });

        assertEquals("Failed to create car", exception.getMessage());
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void testUpdateCar_Success() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenReturn(car);
        when(carMapper.toDto(any(Car.class))).thenReturn(availableCarDto);

        AvailableCarDto result = carService.update(1L, updateCarDto);

        assertNotNull(result);
        verify(carMapper, times(1)).updateCarFromUpdateCar(any(UpdateCarDto.class), any(Car.class));
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void testUpdateCar_NotFound() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        CarNotFoundException exception = assertThrows(CarNotFoundException.class, () -> {
            carService.update(1L, updateCarDto);
        });

        assertEquals("Car not found", exception.getMessage());
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void testDeleteCar_Success() {
        doNothing().when(carRepository).deleteById(1L);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        carService.delete(1L);

        verify(carRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCar_Exception() {
        CarNotFoundException exception = assertThrows(CarNotFoundException.class, () -> {
            carService.delete(1L);
        });

        assertEquals("Car not found", exception.getMessage());
    }

    @Test
    void testGetAvailableCars_Success() {
        when(carRepository.findAllByStatusAndRentalPointId(any(CarStatus.class), anyLong()))
                .thenReturn(Optional.of(Collections.singletonList(car)));
        when(carMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(availableCarDto));

        List<AvailableCarDto> result = carService.getAvailableCars(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testGetAvailableCars_NotFound() {
        when(carRepository.findAll()).thenReturn(Collections.singletonList(car));
        when(carMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(availableCarDto));

        List<AvailableCarDto> result = carService.getAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }



    @Test
    void testGetAllCars_Success() {
        when(carRepository.findAll()).thenReturn(List.of(car));
        when(carMapper.toDtoList(anyList())).thenReturn(List.of(availableCarDto));

        List<AvailableCarDto> result = carService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testGetAllCars_Exception() {
        when(carRepository.findAll()).thenThrow(new CarException("Failed to fetch"));

        CarException exception = assertThrows(CarException.class, () -> {
            carService.getAll();
        });

        assertEquals("Failed to fetch", exception.getMessage());
    }
}