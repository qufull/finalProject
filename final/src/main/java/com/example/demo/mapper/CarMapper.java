package com.example.demo.mapper;

import com.example.demo.dto.AvailableCarDto;
import com.example.demo.dto.CarDto;
import com.example.demo.dto.UpdateCarDto;
import com.example.demo.dto.UpdateUser;
import com.example.demo.model.Car;
import com.example.demo.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CarMapper {

    Car dtoToCar(AvailableCarDto dto);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "pricePerMinute", source = "pricePerMinute")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCarFromUpdateCar(UpdateCarDto updateCar, @MappingTarget Car car);

    @Mapping(target = "type", source = "type")
    @Mapping(target = "status", source = "status")
    AvailableCarDto toDto(Car car);
    List<AvailableCarDto> toDtoList(List<Car> cars);


    CarDto toCarDto(Car car);

}
