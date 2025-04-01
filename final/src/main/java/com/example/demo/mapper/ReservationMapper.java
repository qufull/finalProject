package com.example.demo.mapper;

import com.example.demo.dto.EndReservationDto;
import com.example.demo.dto.StartReservationDto;
import com.example.demo.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservationMapper {

    @Mapping(target = "totalCost", source = "amount")
    EndReservationDto toEndReservationDto(Double amount) ;

    @Mapping(target = "make", source = "car.make")
    @Mapping(target = "model", source = "car.model")
    @Mapping(target = "year", source = "car.year")
    @Mapping(target = "type", source = "car.type")
    @Mapping(target = "startTime", source = "startTime")
    StartReservationDto toStartReservationDto(Reservation reservation) ;
}
