package com.andreibancos.webchatbackend;

import com.andreibancos.webchatbackend.dto.DisplayMessageDto;
import com.andreibancos.webchatbackend.dto.DisplayUserDto;
import com.andreibancos.webchatbackend.dto.RegisterUserDto;
import com.andreibancos.webchatbackend.entity.Message;
import com.andreibancos.webchatbackend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IGeneralMapper {
    User registerUserDtoToUser(RegisterUserDto registerUserDto);
    DisplayUserDto userToDisplayUserDto(User user);

    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "receiver.id", target = "receiverId")
    DisplayMessageDto messageToDisplayMessageDto(Message message);
}