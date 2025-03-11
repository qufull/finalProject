package com.example.demo.mapper;

import com.example.demo.dto.EndReservationDto;
import com.example.demo.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservationMapper {
    @Mapping(source = "totalCost",target = "totalCost")
    EndReservationDto toEndReservationDto(Reservation reservation);
}
