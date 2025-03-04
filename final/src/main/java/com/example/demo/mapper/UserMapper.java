package com.example.demo.mapper;

import com.example.demo.dto.UpdateUser;
import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "credential.username", source = "username")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUpdateUser(UpdateUser updateUser, @MappingTarget User user);

    @Mapping(source = "credential.username", target = "username")
    UserDto userToUserDto(User user);
}
