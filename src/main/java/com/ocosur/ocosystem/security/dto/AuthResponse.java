package com.ocosur.ocosystem.security.dto;

public class AuthResponse {

    private String token;
    private UserSessionDTO user;

    public AuthResponse(String token, UserSessionDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public UserSessionDTO getUser() {
        return user;
    }
}
