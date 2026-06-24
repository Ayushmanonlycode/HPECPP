package com.showdown.springboot.userservice.controller;

import com.showdown.springboot.userservice.dto.AuthResponseDto;
import com.showdown.springboot.userservice.dto.UserLoginDto;
import com.showdown.springboot.userservice.dto.UserProfileDto;
import com.showdown.springboot.userservice.dto.UserRegistrationDto;
import com.showdown.springboot.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User registration, login, and profile management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account. Public endpoint.")
    @ApiResponse(responseCode = "201", description = "User successfully registered")
    @ApiResponse(responseCode = "409", description = "Username or email already exists")
    public ResponseEntity<UserProfileDto> register(@Valid @RequestBody UserRegistrationDto dto) {
        UserProfileDto profile = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(profile);
    }

    @PostMapping("/login")
    @Operation(summary = "Login with username and password", description = "Validates credentials. Public endpoint. For JWT auth use Keycloak token endpoint.")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody UserLoginDto dto) {
        AuthResponseDto response = userService.login(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserProfileDto>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getProfile(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDto> updateProfile(@PathVariable UUID id,
                                                        @Valid @RequestBody UserRegistrationDto dto) {
        return ResponseEntity.ok(userService.updateProfile(id, dto));
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<UserProfileDto> partialUpdateProfile(@PathVariable UUID id,
                                                               @Valid @RequestBody com.showdown.springboot.userservice.dto.UserProfileUpdateDto dto) {
        return ResponseEntity.ok(userService.partialUpdateProfile(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
