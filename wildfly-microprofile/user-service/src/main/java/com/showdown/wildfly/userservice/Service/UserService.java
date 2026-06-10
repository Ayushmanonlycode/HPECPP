package com.showdown.wildfly.userservice.Service;

import com.showdown.wildfly.userservice.Models.DTO.UserDto;
import com.showdown.wildfly.userservice.Models.User;
import com.showdown.wildfly.userservice.Repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

   public void addUser(UserDto dto) {

       User u= new User();
       u.setAddress(dto.getAddress());
       u.setEmail(dto.getEmail());
       u.setName(dto.getName());
       u.setPhoneNumber(dto.getPhoneNumber());
       u.setRole(dto.getRole());
       u.setPassword(dto.getPassword());

        if(userRepository.findByEmail(u.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        userRepository.addUser(u);
    }

    public User getUserById(String id) {
        return userRepository.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User updateUser(User user) {
        return userRepository.updateUser(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteUser(id);
    }
    public User login(String email, String password) {
        return userRepository.login(email, password);
    }
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
    public long countUsers() {
        return userRepository.countUsers();
    }
}