package com.example.demo.mapper;

import com.example.demo.dto.PenaltiesDto;
import com.example.demo.dto.PenaltyDto;
import com.example.demo.model.Penalty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PenaltyMapper {
    @Mapping(target = "make", source = "car.make")
    @Mapping(target = "model", source = "car.model")
    @Mapping(target = "year", source = "car.year")
    @Mapping(target = "type", source = "car.type")
    PenaltiesDto toDto(Penalty penalty);

    List<PenaltiesDto> toDtos(List<Penalty> penalties);


    PenaltyDto toPenaltyDto(Penalty penalty);

}
