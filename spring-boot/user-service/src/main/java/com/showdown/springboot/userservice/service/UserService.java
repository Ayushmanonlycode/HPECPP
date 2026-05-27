package com.showdown.springboot.userservice.service;

import com.showdown.springboot.userservice.dto.UserLoginDto;
import com.showdown.springboot.userservice.dto.UserProfileDto;
import com.showdown.springboot.userservice.dto.UserRegistrationDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserProfileDto register(UserRegistrationDto dto);

    UserProfileDto login(UserLoginDto dto);

    UserProfileDto getProfile(UUID id);

    UserProfileDto updateProfile(UUID id, UserRegistrationDto dto);

    List<UserProfileDto> listUsers();

    void deleteUser(UUID id);
}
