package com.showdown.wildfly.userservice.Controllers;

import com.showdown.wildfly.userservice.Models.User;
import com.showdown.wildfly.userservice.Service.UserService;
import com.showdown.wildfly.userservice.Models.DTO.LoginDto;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;

    @POST
    @Path("/register")
    @Transactional
    public User addUser(User user) {

        userService.addUser(user);

        return user;
    }

    @GET
    @Path("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GET
    @Path("/{id}")
    public User getUserById(@PathParam("id") String id) {
        return userService.getUserById(id);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public String deleteUser(@PathParam("id") String id) {
        userService.deleteUser(id);
        return "User Deleted Successfully";
    }
    @PUT
    @Path("/{id}/profile")
    @Transactional
    public User updateUser(
            @PathParam("id") String id,
            User user) {

        user.setId(id);

        return userService.updateUser(user);
    }
    @POST
    @Path("/login")
    public User login(LoginDto loginDto) {

        return userService.login(
                loginDto.getUsername(),
                loginDto.getPassword());
    }
}