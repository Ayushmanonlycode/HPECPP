package com.showdown.wildfly.userservice.Models.DTO;

import com.showdown.wildfly.userservice.Models.User;

public class AuthResponseDto {

    private String token;
    private String tokenType = "Bearer";
    // NOTE: this field name is part of the wire contract with the frontend.
    // customer-experience-service/src/services/authApi.ts reads `res.data.user`
    // (a contract it shares with spring-boot's intended response shape) —
    // it must stay "user" here, not "profile", or every login/register call
    // resolves to `undefined` client-side and crashes on `profile.id`.
    private User user;

    public AuthResponseDto() {}

    public AuthResponseDto(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
