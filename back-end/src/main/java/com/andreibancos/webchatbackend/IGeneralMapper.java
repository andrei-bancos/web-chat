package com.andreibancos.webchatbackend;

import com.andreibancos.webchatbackend.dto.RegisterUserDto;
import com.andreibancos.webchatbackend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IGeneralMapper {
    User registerUserDtoToUser(RegisterUserDto registerUserDto);
}