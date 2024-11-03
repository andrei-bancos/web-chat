package com.andreibancos.webchatbackend.controller;

import com.andreibancos.webchatbackend.IGeneralMapper;
import com.andreibancos.webchatbackend.dto.RegisterUserDto;
import com.andreibancos.webchatbackend.entity.User;
import com.andreibancos.webchatbackend.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
}
