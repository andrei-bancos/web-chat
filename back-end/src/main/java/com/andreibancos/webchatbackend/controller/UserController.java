package com.andreibancos.webchatbackend.controller;

import com.andreibancos.webchatbackend.IGeneralMapper;
import com.andreibancos.webchatbackend.dto.ChangeUserPasswordDto;
import com.andreibancos.webchatbackend.dto.DisplayUserDto;
import com.andreibancos.webchatbackend.dto.RegisterUserDto;
import com.andreibancos.webchatbackend.entity.User;
import com.andreibancos.webchatbackend.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/user")
@Tag(name = "User")
public class UserController {
    private final IUserService userService;
    private final IGeneralMapper generalMapper;

    UserController(IUserService userService, IGeneralMapper generalMapper) {
        this.userService = userService;
        this.generalMapper = generalMapper;
    }

    @GetMapping("/authenticated")
    @Operation(summary = "Get authenticated user details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return user details."),
            @ApiResponse(responseCode = "404", description = "Not found.")
    })
    public ResponseEntity<DisplayUserDto> getAuthenticatedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByUsername(authentication.getName());
            DisplayUserDto displayUserDto = generalMapper.userToDisplayUserDto(user);
            return ResponseEntity.ok(displayUserDto);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has been created!"),
            @ApiResponse(responseCode = "400", description = "Bad request.")
    })
    public ResponseEntity<Map<String, String>> createUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        User user = generalMapper.registerUserDtoToUser(registerUserDto);
        userService.createUser(user);
        return ResponseEntity.ok(Map.of("message", "User has been created!"));
    }

    @PutMapping("/password")
    @Operation(summary = "Change user password")
    public ResponseEntity<Map<String, String>> updateUserPassword(@RequestBody ChangeUserPasswordDto changeUserPasswordDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByUsername(authentication.getName());
            userService.updateUserPassword(user, changeUserPasswordDto);
            return ResponseEntity.ok(Map.of("message", "Password has been updated!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    @Operation(summary = "Delete user")
    public ResponseEntity<String> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByUsername(authentication.getName());
            userService.deleteUser(user.getId());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contacts")
    @Operation(summary = "Get user contacts")
    public ResponseEntity<Set<DisplayUserDto>> getUserContacts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByUsername(authentication.getName());
            return ResponseEntity.ok(userService.getUserContacts(user.getId()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/contact/{userContactId}")
    @Operation(summary = "Get contact user information's")
    public ResponseEntity<DisplayUserDto> getUserContact(@PathVariable UUID userContactId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            userService.getUserByUsername(authentication.getName());
            return ResponseEntity.ok(userService.getUserContact(userContactId));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/contact/{usernameContact}")
    @Operation(summary = "User add contact")
    public ResponseEntity<Map<String, String>> addContact(@PathVariable String usernameContact) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByUsername(authentication.getName());
            userService.addContact(user.getId(), usernameContact);
            return ResponseEntity.ok(Map.of("message", "Contact has been added!"));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/contact/{userContactId}")
    @Operation(summary = "Delete contact")
    public ResponseEntity<String> deleteContact(@PathVariable UUID userContactId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByUsername(authentication.getName());
            userService.deleteContact(user.getId(), userContactId);
        }
        return ResponseEntity.noContent().build();
    }
}
