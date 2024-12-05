package com.andreibancos.webchatbackend.controller;

import com.andreibancos.webchatbackend.dto.DisplayMessageDto;
import com.andreibancos.webchatbackend.dto.LastChatsDto;
import com.andreibancos.webchatbackend.dto.SendMessageDto;
import com.andreibancos.webchatbackend.entity.User;
import com.andreibancos.webchatbackend.service.IMessageService;
import com.andreibancos.webchatbackend.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/messages")
public class MessageController {
    private final IMessageService messageService;
    private final IUserService userService;

    public MessageController(IMessageService messageService, IUserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/{senderId}/chat/{receivedId}")
    public ResponseEntity<List<DisplayMessageDto>> getChatMessages(
            @PathVariable UUID senderId,
            @PathVariable UUID receivedId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            userService.getUserByUsername(authentication.getName());
            return ResponseEntity.ok(messageService.getChatMessages(senderId, receivedId));
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/chats")
    public ResponseEntity<List<LastChatsDto>> getLastChats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByUsername(authentication.getName());
            List<LastChatsDto> lastChatsDto = messageService.getLastChats(user.getId());
            return ResponseEntity.ok(lastChatsDto);
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/{receiverId}")
    public ResponseEntity<DisplayMessageDto> sendChatMessage(
            @Valid @RequestBody SendMessageDto sendMessageDto,
            @PathVariable UUID receiverId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            userService.getUserByUsername(authentication.getName());
            DisplayMessageDto message = messageService.sendMessage(receiverId, sendMessageDto);
            messageService.sendMessageToUserWS(receiverId, message);
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.status(401).build();
    }

    @PutMapping("/{messageId}/markAsRead")
    public ResponseEntity<Map<String, String>> markMessageAsRead(@PathVariable UUID messageId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()) {
            messageService.markAsRead(messageId);
            return ResponseEntity.ok(Map.of("message:", "Message marked as read"));
        }

        return ResponseEntity.status(401).build();
    }
}
