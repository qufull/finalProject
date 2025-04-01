package com.example.demo.mapper;

import com.example.demo.dto.DriverLicensDto;
import com.example.demo.model.DriverLicens;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DriverLicensMapper {

    @Mapping(source = "number",target = "number")
    @Mapping(source = "dataOfIssue",target = "dataOfIssue")
    @Mapping(source = "dataOfExpity",target = "dataOfExpity")
    DriverLicensDto toDriverLicensDto(DriverLicens driverLicens);

    @Mapping(source = "number",target = "number")
    @Mapping(source = "dataOfIssue",target = "dataOfIssue")
    @Mapping(source = "dataOfExpity",target = "dataOfExpity")
    DriverLicens toDriverLicens(DriverLicensDto driverLicensDto);
}
