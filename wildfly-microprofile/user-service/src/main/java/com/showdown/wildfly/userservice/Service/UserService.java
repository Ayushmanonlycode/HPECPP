package com.showdown.wildfly.userservice.Service;

import com.showdown.wildfly.userservice.Models.User;
import com.showdown.wildfly.userservice.Models.DTO.AuthResponseDto;
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

    @Inject
    KeycloakService keycloakService;
    public void addUser(User user) {

        if(userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        if(userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        if(user.getStatus() == null) {
            user.setStatus("ACTIVE");
        }

        user.setId(UUID.randomUUID().toString());

        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        try {

            String kcUserId =
                    keycloakService.createUser(user);

            keycloakService.setPassword(
                    kcUserId,
                    user.getPassword());

            keycloakService.assignCustomerRole(
                    kcUserId);

        } catch(Exception e) {

            throw new RuntimeException(
                    "Keycloak registration failed",
                    e);
        }
        System.out.println("REGISTER PASSWORD = [" + user.getPassword() + "]");
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

        // This endpoint is used for partial profile updates (e.g. saving a
        // shipping address at checkout), and the request body is bound
        // directly onto this JPA entity. Any field the caller didn't send
        // comes in as null — and since updateUser() below does a full
        // em.merge(), a null here would silently overwrite (wipe) that
        // column in the DB. username/password/email/status are never part
        // of a profile-update request, so always carry the existing values
        // forward for them. Only genuinely blankable profile fields are
        // allowed to come from the incoming object as-is.
        user.setUsername(existingUser.getUsername());
        user.setPassword(existingUser.getPassword());
        user.setEmail(existingUser.getEmail());
        user.setStatus(existingUser.getStatus());
        user.setCreatedAt(existingUser.getCreatedAt());

        user.setUpdatedAt(Instant.now());

        return userRepository.updateUser(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteUser(id);
    }
    public AuthResponseDto login(String username, String password) {
        System.out.println("LOGIN USERNAME = " + username);
        System.out.println("LOGIN PASSWORD = " + password);
        User user =
                userRepository.login(username, password);

        if(user == null){
            throw new RuntimeException("Invalid username/password");
        }

        try {

            String token =
                    keycloakService.authenticate(username,password);

            return new AuthResponseDto(token,user);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }

    }
    public boolean checkUser(String id){
       return userRepository.checkUser(id);
    }
}