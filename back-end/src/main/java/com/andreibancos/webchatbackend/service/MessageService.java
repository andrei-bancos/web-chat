package com.andreibancos.webchatbackend.service;

import com.andreibancos.webchatbackend.IGeneralMapper;
import com.andreibancos.webchatbackend.dto.DisplayMessageDto;
import com.andreibancos.webchatbackend.dto.LastChatsDto;
import com.andreibancos.webchatbackend.dto.SendMessageDto;
import com.andreibancos.webchatbackend.entity.Message;
import com.andreibancos.webchatbackend.entity.User;
import com.andreibancos.webchatbackend.repository.IMessageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageService implements IMessageService {
    private final IMessageRepository messageRepository;
    private final IUserService userService;
    private final IGeneralMapper generalMapper;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageService(
            IMessageRepository messageRepository,
            IUserService userService,
            IGeneralMapper generalMapper,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.generalMapper = generalMapper;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public List<DisplayMessageDto> getChatMessages(UUID userSenderId, UUID userReceiverId) {
        List<Message> messages = messageRepository.findAllMessagesBetweenUsers(userSenderId, userReceiverId);
        return messages.stream()
                .map(generalMapper::messageToDisplayMessageDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LastChatsDto> getLastChats(UUID userId) {
        Pageable pageable = PageRequest.of(0, 10);
        List<Message> messages = messageRepository.findLastMessagesWithUsers(userId, pageable);

        List<LastChatsDto> lastChats = new ArrayList<>();

        for (Message message : messages) {
            UUID otherUserId = message.getSender().getId().equals(userId) ?
                               message.getReceiver().getId() : message.getSender().getId();
            LastChatsDto lastChat = getLastChatsDto(userId, message, otherUserId);
            lastChats.add(lastChat);
        }

        return lastChats;
    }

    private static LastChatsDto getLastChatsDto(UUID userId, Message message, UUID otherUserId) {
        String firstName = message.getSender().getId().equals(userId) ?
                           message.getReceiver().getFirstName() : message.getSender().getFirstName();
        String lastName = message.getSender().getId().equals(userId) ?
                          message.getReceiver().getLastName() : message.getSender().getLastName();
        String lastMessage = message.getContent();
        Date createdAt = message.getCreatedAt();

        return new LastChatsDto(otherUserId, firstName, lastName, lastMessage, message.getRead(), createdAt);
    }

    @Override
    public DisplayMessageDto sendMessage(UUID receiverId, SendMessageDto sendMessageDto) {
        User sender =  userService.getUserById(sendMessageDto.getSenderId());
        User receiver =  userService.getUserById(receiverId);

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(sendMessageDto.getContent());

        messageRepository.save(message);

        return generalMapper.messageToDisplayMessageDto(message);
    }

    public void sendMessageToUserWS(UUID receiverId, DisplayMessageDto message) {
        User receiverUser = userService.getUserById(receiverId);
        messagingTemplate.convertAndSendToUser(
                receiverUser.getUsername(),
                "/queue/messages",
                message
        );
    }

    @Override
    public boolean markAsRead(UUID messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() ->
                new EntityNotFoundException("Message not found")
        );

        message.setRead(true);
        messageRepository.save(message);
        return true;
    }
}
