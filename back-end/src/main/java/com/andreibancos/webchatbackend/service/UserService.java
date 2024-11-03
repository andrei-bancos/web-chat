package com.andreibancos.webchatbackend.service;

import com.andreibancos.webchatbackend.entity.User;
import com.andreibancos.webchatbackend.repository.IUserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    private final IUserRepository userRepository;

    public UserService(IUserRepository IUserRepository) {
        this.userRepository = IUserRepository;
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("User not found")
        );
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("User not found")
        );
    }

    @Override
    public void createUser(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        user.setUsername(user.getUsername().toLowerCase());

        Optional<User> existingUserByEmail = userRepository.getUserByEmail(user.getEmail());
        Optional<User> existingUserByUserName = userRepository.getUserByUsername(user.getUsername());

        if (existingUserByEmail.isPresent()) {
            throw new EntityExistsException("User with this email already exists");
        }

        if (existingUserByUserName.isPresent()) {
            throw new EntityExistsException("Username already exists");
        }


        var bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        User userToUpdate = getUserById(user.getId());

        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setEmail(user.getEmail());

        return userRepository.save(userToUpdate);
    }

    @Override
    public void deleteUser(UUID id) {
        User userToDelete = getUserById(id);
        userRepository.delete(userToDelete);
    }
}
