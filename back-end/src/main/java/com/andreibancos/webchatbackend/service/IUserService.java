package com.andreibancos.webchatbackend.service;

import com.andreibancos.webchatbackend.dto.ChangeUserPasswordDto;
import com.andreibancos.webchatbackend.dto.DisplayUserDto;
import com.andreibancos.webchatbackend.entity.User;

import java.util.Set;
import java.util.UUID;

public interface IUserService {
    User getUserById(UUID id);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    void createUser(User user);
    void updateUserPassword(User user, ChangeUserPasswordDto changeUserPasswordDto);
    void deleteUser(UUID id);
    DisplayUserDto getUserContact(UUID userContactId);
    void addContact(UUID userId, String usernameContact);
    void deleteContact(UUID userId, UUID userContactId);
    Set<DisplayUserDto> getUserContacts(UUID userId);
}
