package com.andreibancos.webchatbackend.service;

import com.andreibancos.webchatbackend.dto.DisplayMessageDto;
import com.andreibancos.webchatbackend.dto.LastChatsDto;
import com.andreibancos.webchatbackend.dto.SendMessageDto;

import java.util.List;
import java.util.UUID;

public interface IMessageService {
    List<DisplayMessageDto> getChatMessages(UUID userSenderId, UUID userReceiverId);
    List<LastChatsDto> getLastChats(UUID userId);
    DisplayMessageDto sendMessage(UUID receiverId, SendMessageDto message);
}
