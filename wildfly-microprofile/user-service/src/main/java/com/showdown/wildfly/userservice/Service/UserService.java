package com.showdown.wildfly.userservice.Service;

import com.showdown.wildfly.userservice.Models.User;
import com.showdown.wildfly.userservice.Repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

   public void addUser(User user) {

        if(userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        if(user.getStatus() == null) {
            user.setStatus("ACTIVE");
        }
        user.setId(UUID.randomUUID().toString());

        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        userRepository.addUser(user);
    }
    
    public User getUserById(String id) {
        return userRepository.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User updateUser(User user) {

        User existingUser =
                userRepository.getUserById(user.getId());

        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        user.setCreatedAt(existingUser.getCreatedAt());

        user.setUpdatedAt(Instant.now());

        return userRepository.updateUser(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteUser(id);
    }
    public User login(String username, String password) {
        return userRepository.login(username, password);
    }

    public boolean checkUser(String id){
       return userRepository.checkUser(id);
    }
}