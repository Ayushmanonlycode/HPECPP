package com.showdown.springboot.userservice.service;

import com.showdown.springboot.userservice.dto.UserLoginDto;
import com.showdown.springboot.userservice.dto.UserProfileDto;
import com.showdown.springboot.userservice.dto.UserRegistrationDto;
import com.showdown.springboot.userservice.entity.User;
import com.showdown.springboot.userservice.exception.DuplicateResourceException;
import com.showdown.springboot.userservice.exception.ResourceNotFoundException;
import com.showdown.springboot.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserProfileDto register(UserRegistrationDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("username", dto.getUsername());
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("email", dto.getEmail());
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // plain-text for demo
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setZip(dto.getZip());
        user.setCountry(dto.getCountry());

        User saved = userRepository.save(user);
        return toProfileDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto login(UserLoginDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return toProfileDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getProfile(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return toProfileDto(user);
    }

    @Override
    public UserProfileDto updateProfile(UUID id, UserRegistrationDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        // Check for duplicate username/email only if changed
        if (!user.getUsername().equals(dto.getUsername())
                && userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("username", dto.getUsername());
        }
        if (!user.getEmail().equals(dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("email", dto.getEmail());
        }

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setZip(dto.getZip());
        user.setCountry(dto.getCountry());

        User saved = userRepository.save(user);
        return toProfileDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileDto> listUsers() {
        return userRepository.findAll().stream()
                .map(this::toProfileDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }

    // ── Mapping helper ───────────────────────────────────────────

    private UserProfileDto toProfileDto(User user) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setState(user.getState());
        dto.setZip(user.getZip());
        dto.setCountry(user.getCountry());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
