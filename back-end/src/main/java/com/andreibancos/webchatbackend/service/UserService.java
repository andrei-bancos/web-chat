package com.andreibancos.webchatbackend.service;

import com.andreibancos.webchatbackend.IGeneralMapper;
import com.andreibancos.webchatbackend.dto.ChangeUserPasswordDto;
import com.andreibancos.webchatbackend.dto.DisplayUserDto;
import com.andreibancos.webchatbackend.entity.User;
import com.andreibancos.webchatbackend.repository.IUserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final IGeneralMapper mapper;

    public UserService(IUserRepository IUserRepository, IGeneralMapper mapper) {
        this.userRepository = IUserRepository;
        this.mapper = mapper;
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

    @Override
    public void addContact(UUID userId,  String usernameContact) {
        User user = getUserById(userId);
        User userContact = getUserByUsername(usernameContact.toLowerCase());

        if(user.getUsername().equals(userContact.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This is your username.");
        }

        user.addContact(userContact);
        userRepository.save(user);
    }

    @Override
    public void deleteContact(UUID userId, UUID userContactId) {
        User user = getUserById(userId);
        User userContact = getUserById(userContactId);

        user.removeContact(userContact);
        userRepository.save(user);
    }

    @Override
    public Set<DisplayUserDto> getUserContacts(UUID userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get().getContacts().stream()
                    .map(mapper::userToDisplayUserDto)
                    .collect(Collectors.toSet());
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public DisplayUserDto getUserContact(UUID userContactId) {
        User user = getUserById(userContactId);
        return mapper.userToDisplayUserDto(user);
    }
}
