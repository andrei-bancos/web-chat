package com.andreibancos.webchatbackend.service;

import com.andreibancos.webchatbackend.dto.ChangeUserPasswordDto;
import com.andreibancos.webchatbackend.entity.User;
import com.andreibancos.webchatbackend.repository.IUserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public void updateUserPassword(User user, ChangeUserPasswordDto changeUserPasswordDto) {
        User userToUpdate = getUserById(user.getId());

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if(bCryptPasswordEncoder.matches(changeUserPasswordDto.getOldPassword(), userToUpdate.getPassword())) {
            userToUpdate.setPassword(bCryptPasswordEncoder.encode(changeUserPasswordDto.getNewPassword()));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password does not match");
        }

        userRepository.save(userToUpdate);
    }

    @Override
    public void deleteUser(UUID id) {
        User userToDelete = getUserById(id);
        userRepository.delete(userToDelete);
    }
}
