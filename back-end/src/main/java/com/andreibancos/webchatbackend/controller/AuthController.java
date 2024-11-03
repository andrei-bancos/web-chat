package com.andreibancos.webchatbackend.controller;

import com.andreibancos.webchatbackend.dto.LoginUserDto;
import com.andreibancos.webchatbackend.entity.User;
import com.andreibancos.webchatbackend.service.AuthService;
import com.andreibancos.webchatbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/auth")
@Tag(name = "Auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @Value("${spring.application.production}")
    private Boolean isProduction;

    AuthController(UserService userService, AuthService authService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(path = "/login")
    @Operation(summary = "Authenticate a user and return a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful, returns a JWT token"),
            @ApiResponse(responseCode = "401", description = "Incorrect email or password.")
    })
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody LoginUserDto loginUserDto,
            HttpServletResponse response
    ) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword())
            );

            User user = userService.getUserByEmail(loginUserDto.getEmail());
            String jwtToken = authService.createJwtToken(user);

            ResponseCookie cookie = ResponseCookie.from("JWT", jwtToken)
                    .httpOnly(true)
                    .secure(isProduction)
                    .sameSite("strict")
                    .maxAge(24 * 3600)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok(Map.of("message", "User logged in, cookie set"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message","Incorrect email or password")
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message","Authentication failed")
            );
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @GetMapping("/validate-token")
    @Operation(summary = "Validate the JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Token is invalid or expired")
    })
    public ResponseEntity<Map<String, String>> validateToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            try {
                userService.getUserByUsername(username);
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        Map.of("message", "Token is invalid or expired")
                );
            }
            return ResponseEntity.ok(
                    Map.of("message","Token is valid")
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message","Token is invalid or expired")
            );
        }
    }
}
