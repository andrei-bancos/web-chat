package com.andreibancos.webchatbackend.service;

import com.andreibancos.webchatbackend.entity.User;

import java.util.UUID;

public interface IUserService {
    User getUserById(UUID id);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    void createUser(User user);
    User updateUser(User user);
    void deleteUser(UUID id);
}
