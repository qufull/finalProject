package com.example.demo.mapper;

import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.dto.UpdateUser;
import com.example.demo.model.Credential;
import com.example.demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CredentialMapper {

    void updateUserFromUpdateUser(ChangePasswordDto dto, @MappingTarget Credential credential);

    ChangePasswordDto changeCredentialToChangePasswordDto(Credential credential);
}
