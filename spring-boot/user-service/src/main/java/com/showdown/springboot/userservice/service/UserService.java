package com.showdown.springboot.userservice.service;


import com.showdown.springboot.userservice.dto.UserProfileDto;
import com.showdown.springboot.userservice.dto.UserProfileUpdateDto;
import com.showdown.springboot.userservice.dto.UserRegistrationDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserProfileDto register(UserRegistrationDto dto);


    UserProfileDto getProfile(UUID id);

    UserProfileDto updateProfile(UUID id, UserRegistrationDto dto);

    UserProfileDto partialUpdateProfile(UUID id, UserProfileUpdateDto dto);

    List<UserProfileDto> listUsers();

    void deleteUser(UUID id);
}
