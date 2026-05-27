package com.showdown.springboot.userservice.service;

import com.showdown.springboot.userservice.dto.UserLoginDto;
import com.showdown.springboot.userservice.dto.UserProfileDto;
import com.showdown.springboot.userservice.dto.UserRegistrationDto;
import com.showdown.springboot.userservice.entity.User;
import com.showdown.springboot.userservice.exception.DuplicateResourceException;
import com.showdown.springboot.userservice.exception.ResourceNotFoundException;
import com.showdown.springboot.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void register_whenValidDto_shouldSaveAndReturnProfile() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("john_doe");
        dto.setEmail("john@example.com");
        dto.setPassword("secret");
        dto.setFirstName("John");
        dto.setLastName("Doe");

        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setUsername("john_doe");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("secret");
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");

        when(userRepository.existsByUsername("john_doe")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserProfileDto result = userService.register(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(savedUser.getId());
        assertThat(result.getUsername()).isEqualTo("john_doe");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_whenUsernameExists_shouldThrowDuplicateResourceException() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("existing");
        dto.setEmail("unique@example.com");

        when(userRepository.existsByUsername("existing")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("username");
    }

    @Test
    void register_whenEmailExists_shouldThrowDuplicateResourceException() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("unique");
        dto.setEmail("existing@example.com");

        when(userRepository.existsByUsername("unique")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email");
    }

    @Test
    void login_whenValidCredentials_shouldReturnProfile() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setUsername("john_doe");
        loginDto.setPassword("secret");

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("john_doe");
        user.setPassword("secret");

        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

        UserProfileDto result = userService.login(loginDto);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("john_doe");
    }

    @Test
    void login_whenInvalidPassword_shouldThrowIllegalArgumentException() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setUsername("john_doe");
        loginDto.setPassword("wrong");

        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("secret");

        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.login(loginDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid username or password");
    }

    @Test
    void getProfile_whenExists_shouldReturnProfile() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        user.setUsername("john_doe");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserProfileDto result = userService.getProfile(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    void getProfile_whenNotExists_shouldThrowResourceNotFoundException() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getProfile(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
